import json

en_us_path = '/mnt/ZakuraOnline/ZakuraProject/Minecraft Resource Project/Thai alphabet block/common/src/main/resources/assets/thai_alphabet_block/lang/en_us.json'
th_th_path = '/mnt/ZakuraOnline/ZakuraProject/Minecraft Resource Project/Thai alphabet block/common/src/main/resources/assets/thai_alphabet_block/lang/th_th.json'

with open(en_us_path, 'r', encoding='utf-8') as f:
    en_us = json.load(f)

# Add empty_block if missing
if "block.thai_alphabet_block.empty_block" not in en_us:
    en_us["block.thai_alphabet_block.empty_block"] = "Thai Alphabet Block"

# Sort keys alphabetically for better organization
en_us = dict(sorted(en_us.items()))

with open(en_us_path, 'w', encoding='utf-8') as f:
    json.dump(en_us, f, ensure_ascii=False, indent=2)

# Create th_th content
th_th = {
    "itemGroup.thai_alphabet_block": "บล็อกอักษรไทย",
    "thai_alphabet_block.tip.dye": "คลิกขวาด้วยสีย้อมเพื่อเปลี่ยนสีบล็อก"
}

for key, value in en_us.items():
    if key.startswith("block.thai_alphabet_block."):
        # Extract the Thai character part from "บ (Bo Baimai)" -> "บ"
        if '(' in value:
            thai_char = value.split('(')[0].strip()
            th_th[key] = f"บล็อกตัวอักษร {thai_char}"
        else:
            th_th[key] = f"บล็อก {value}"

th_th["block.thai_alphabet_block.empty_block"] = "บล็อกอักษรไทย (ว่าง)"

# Sort th_th keys
th_th = dict(sorted(th_th.items()))

with open(th_th_path, 'w', encoding='utf-8') as f:
    json.dump(th_th, f, ensure_ascii=False, indent=2)

print("Updated lang files.")
