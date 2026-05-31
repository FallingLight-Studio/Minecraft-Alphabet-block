# Review: Shift+Right-Click Glyph Color Dyeing Feature

This document presents a comprehensive technical review ofฟ the proposed implementation plan for the **Shift+Right-Click Glyph Color Dyeing** feature.

---

## 1. Executive Summary

*   **Feasibility:** **Excellent**. The plan is highly feasible and integrates naturally with the existing architecture of the Thai Alphabet Block mod.
*   **Structure:** **Well-designed**. Modifying the property local to each loader's `ThaiAlphabetColorProperties` (rather than forcing a common refactoring) minimizes compile-time breakage.
*   **Compatibility:** **Safe**. Using the new 1.21.1 blockstate and Data Component systems guarantees seamless support on all three target loaders (Fabric, Forge, NeoForge).

---

## 2. Technical Validation

### 2.1. BlockState Property and Multipart JSONs
*   **The Problem:** Adding `GLYPH_COLOR` introduces a combination of $16 \times 16 = 256$ block states. If using the traditional `variants` format in blockstate JSONs, Minecraft will throw an exception at startup due to missing property mappings unless all 256 combinations are defined.
*   **The Proposed Solution (Multipart):** The proposal to switch from `variants` to `multipart` with an unconditional `apply` is **correct and highly recommended**.
    ```json
    {
      "multipart": [
        { "apply": { "model": "thai_alphabet_block:block/consonant_ko_kai" } }
      ]
    }
    ```
    This completely decouples the block state properties from model rendering. Minecraft will load the single model for every combination of properties.
*   **Alternative (Empty Variant):** Another option is using the empty string variant `""`:
    ```json
    {
      "variants": {
        "": { "model": "thai_alphabet_block:block/consonant_ko_kai" } }
      }
    }
    ```
    This works for full blocks, but **multipart is better** because if any sub-class (like a slab) needs model variations (e.g. `type=top`, `type=bottom`), you can use selective conditional matching under `multipart` without enumerating `color` or `glyph_color`.

### 2.2. Interaction Logic (`useItemOn`)
*   **Key Checker:** `player.isShiftKeyDown()` is checked on both physical client and server. This is 100% safe as the shifting state is synchronized automatically by the network engine.
*   **Result Codes:** Returning `ItemInteractionResult.CONSUME` when the color matches prevents item consumption while stopping other actions, which aligns with vanilla behaviour.
*   **Sound Pitch (0.8F):** Changing the pitch of `DYE_USE` to `0.8F` on the glyph dye action is a great UX touch that gives tactile feedback to players.

### 2.3. Data Components Compatibility
*   The use of `BlockItemStateProperties` via `stateProps.apply(state)` in `ThaiAlphabetBlockStateUtil` is forward-compatible. It automatically serializes and deserializes *all* properties registered on the block, meaning both `color` and `glyph_color` will be saved on items without requiring any extra code!

---

## 3. Answers to Open Questions

1.  **สีเริ่มต้นของ `glyph_color` (Default Color):**
    > **Recommendation:** Yes, keep it as `DyeColor.WHITE` (which translates to `0xFFFFFFFF` / pass-through).
    *   **Why:** Existing blocks in players' worlds will default to `WHITE` upon updating, preserving their original dark brown appearance. If set to another color, all old blocks would suddenly change their glyph color.
2.  **การใช้ White Dye ย้อม Glyph เพื่อรีเซ็ต:**
    > **Recommendation:** Yes, using White Dye should reset the color back to the default dark brown texture.
    *   **Why:** It is intuitive. Since `WHITE` acts as a pass-through color, using a White Dye acts as a natural "un-dye" mechanism to revert the glyph to its default texture.
3.  **Composite Blockstates (เช่น `composite_sara_i_mai_ek.json`):**
    > **Recommendation:** Yes, these must be updated to the `multipart` format as well.
    *   **Why:** The block classes they represent will register both the `COLOR` and `GLYPH_COLOR` properties. If their blockstate JSON does not match all property combinations (either via `multipart` or an empty variant `""`), Minecraft will throw a model loading error on startup.

---

## 4. Implementation Recommendations & Optimizations

### A. Unified Client Color Matching
Make sure that in the item color providers of `ThaiAlphabetFabricClient`, `ThaiAlphabetForgeClient`, and `ThaiAlphabetNeoForgeClient`, you update the `tintIndex == 1` check to fetch the `GLYPH_COLOR` property instead of `COLOR`.

**Example update for `ThaiAlphabetFabricClient.java`:**
```java
// Item Color Provider Registration
ColorProviderRegistry.ITEM.register(
    (ItemStack stack, int tintIndex) -> {
        if (tintIndex != 0 && tintIndex != 1) {
            return -1;
        }
        if (!(stack.getItem() instanceof BlockItem bi) || bi.getBlock() != block) {
            DyeColor white = DyeColor.WHITE;
            return tintIndex == 0
                    ? ThaiAlphabetColorUtil.backgroundArgbFromDye(white)
                    : ThaiAlphabetColorUtil.glyphArgbFromDye(white);
        }
        BlockState state = ThaiAlphabetBlockStateUtil.stateFromItemStack(stack, block);
        
        // Handle background
        if (tintIndex == 0) {
            DyeColor dye = state.hasProperty(ThaiLetterBlock.COLOR)
                    ? state.getValue(ThaiLetterBlock.COLOR)
                    : DyeColor.WHITE;
            return ThaiAlphabetColorUtil.backgroundArgbFromDye(dye);
        }
        
        // Handle glyph
        DyeColor glyphDye = state.hasProperty(ThaiLetterBlock.GLYPH_COLOR)
                ? state.getValue(ThaiLetterBlock.GLYPH_COLOR)
                : DyeColor.WHITE;
        return ThaiAlphabetColorUtil.glyphArgbFromDye(glyphDye);
    },
    block.asItem());
```

### B. Python Script to Automate Blockstate JSON Updates
To update all 69+ blockstate JSON files inside `common/src/main/resources/assets/thai_alphabet_block/blockstates/`, you can use this simple Python script:

```python
import os
import json

path = "common/src/main/resources/assets/thai_alphabet_block/blockstates"

for filename in os.listdir(path):
    if filename.endswith(".json"):
        filepath = os.path.join(path, filename)
        with open(filepath, "r", encoding="utf-8") as f:
            try:
                data = json.load(f)
            except Exception as e:
                print(f"Skipping {filename}: {e}")
                continue
        
        # Check if it uses the old variants format
        if "variants" in data:
            # Extract one model from the variants as the template model
            first_variant = list(data["variants"].values())[0]
            model = first_variant.get("model")
            
            # Construct the new multipart format
            new_data = {
                "multipart": [
                    { "apply": { "model": model } }
                ]
            }
            
            with open(filepath, "w", encoding="utf-8") as f:
                json.dump(new_data, f, indent=2)
            print(f"Updated {filename} to multipart format.")
```

---

## 5. Final Verdict

The implementation plan is **solid and ready to be executed**. Switching to `multipart` blockstates is the cleanest path forward for scaling block properties in Minecraft 1.21.1.
