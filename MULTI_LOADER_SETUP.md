Thai alphabet block/
├── build.gradle                  # Root multi-project configuration
├── gradle.properties             # Version properties for all loaders
├── settings.gradle               # Includes all subprojects
├── MULTI_LOADER_SETUP.md        # This file
│
├── common/                       # Shared logic for all loaders
│   ├── build.gradle             # Common module configuration
│   └── src/main/java/           # Common Java code
│
├── forge/                        # Forge-specific implementation
│   ├── build.gradle             # Forge module configuration
│   └── src/main/java/           # Forge-specific Java code
│       └── com/aitshiroku/thai_alphabet_block/
│           ├── ExampleMod.java  # Forge mod entry point
│           └── Config.java      # Forge config system
│
├── fabric/                       # Fabric-specific implementation
│   ├── build.gradle             # Fabric module configuration
│   └── src/main/java/           # Fabric-specific Java code
│
└── neoforge/                     # NeoForge-specific implementation
    ├── build.gradle             # NeoForge module configuration
    └── src/main/java/           # NeoForge-specific Java code
```

## Module Descriptions

### common/
Contains all code that is shared across all loaders. This includes:
- Core mod logic
- Common interfaces and abstractions
- Common data structures
- Utilities that work on all platforms

**Note:** The common module should NOT contain any loader-specific code.

### forge/
Contains Forge-specific implementation including:
- Forge mod entry point (`@Mod` annotated class)
- Forge event bus subscriptions
- Forge config system (ForgeConfigSpec)
- Forge-specific registries
- Forge-specific resources (mods.toml, etc.)

### fabric/
Contains Fabric-specific implementation including:
- Fabric mod entry point (ModInitializer class)
- Fabric API integrations
- Fabric config system
- Fabric-specific registries
- Fabric-specific resources (fabric.mod.json, etc.)

### neoforge/
Contains NeoForge-specific implementation including:
- NeoForge mod entry point (`@Mod` annotated class)
- NeoForge event bus subscriptions
- NeoForge config system
- NeoForge-specific registries
- NeoForge-specific resources (mods.toml, etc.)

## Building for Different Loaders

### Build for Forge
```bash
./gradlew :forge:build
```

### Build for Fabric
```bash
./gradlew :fabric:build
```

### Build for NeoForge
```bash
./gradlew :neoforge:build
```

### Build for All Loaders
```bash
./gradlew build
```

## Running the Mod

### Run Forge Client
```bash
./gradlew :forge:runClient
```

### Run Fabric Client
```bash
./gradlew :fabric:runClient
```

### Run NeoForge Client
```bash
./gradlew :neoforge:runClient
```

## Version Properties

All version properties are defined in the root `gradle.properties` file:

### Minecraft & Loader Versions
- `minecraft_version` - Target Minecraft version
- `forge_version` - Forge loader version
- `neoforge_version` - NeoForge loader version
- `loader_version` - Fabric loader version
- `yarn_mappings` - Fabric Yarn mappings version
- `fabric_version` - Fabric API version

### Mod Properties
- `mod_id` - Unique mod identifier
- `mod_name` - Display name of the mod
- `mod_version` - Version of the mod
- `mod_group_id` - Maven group ID
- `mod_authors` - Authors of the mod
- `mod_description` - Mod description

## Adding New Code

### Adding Common Code
Place any code that should work on all loaders in `common/src/main/java/`. This code should only use:
- Minecraft base classes
- Common Java libraries
- Your own abstractions

### Adding Loader-Specific Code
Place code specific to a loader in its respective module:
- `forge/src/main/java/` for Forge-specific code
- `fabric/src/main/java/` for Fabric-specific code
- `neoforge/src/main/java/` for NeoForge-specific code

## Next Steps

To complete the multi-loader setup, you need to:

1. **Refactor existing code** - Move common logic from `ExampleMod.java` and `Config.java` into the `common/` module
2. **Create Fabric entry point** - Add a `ModInitializer` class in `fabric/src/main/java/`
3. **Create NeoForge entry point** - Add a `@Mod` annotated class in `neoforge/src/main/java/`
4. **Add Fabric mod metadata** - Create `fabric.mod.json` in `fabric/src/main/resources/`
5. **Add NeoForge mod metadata** - Create `mods.toml` in `neoforge/src/main/resources/META-INF/`
6. **Create common abstractions** - Define interfaces in `common/` for loader-specific implementations

## Best Practices

1. **Keep common code pure** - Avoid loader-specific imports in the common module
2. **Use dependency injection** - Pass loader-specific implementations to common code through constructors or methods
3. **Separate concerns** - Keep registry code, config systems, and event handling loader-specific
4. **Test each loader** - Ensure the mod works correctly on all target loaders before release
5. **Version compatibility** - Update loader versions in `gradle.properties` as needed

## Troubleshooting

### Import Errors
If you see import errors when moving code between modules:
- Ensure you're not importing loader-specific classes in the common module
- Check that common code properly depends only on Minecraft base classes

### Build Failures
If the build fails:
- Verify that all version properties are correctly set in `gradle.properties`
- Check that the correct Minecraft version is being used for all loaders
- Ensure all required repositories are configured in `settings.gradle`

### Runtime Errors
If the mod fails to load:
- Check that mod metadata files (mods.toml, fabric.mod.json) are correctly formatted
- Verify that all required dependencies are declared in each module's `build.gradle`
- Ensure loader-specific entry points are properly annotated

## Resources

- [Forge Documentation](https://docs.minecraftforge.net/)
- [Fabric Documentation](https://fabricmc.net/wiki/)
- [NeoForge Documentation](https://docs.neoforged.net/)
- [Minecraft Mapping Documentation](https://github.com/MinecraftForge/MCPConfig)