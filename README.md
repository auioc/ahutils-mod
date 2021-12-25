<h1 align="center">AHUtils</h1>

<div align="center">

Minecraft utilities, also a library for AH's mods.

[![GitHub license](https://img.shields.io/github/license/WakelessSloth56/ahutils-mod?style=flat-square)](/LICENSE)
[![GitHub Workflow Status](https://img.shields.io/github/workflow/status/WakelessSloth56/ahutils-mod/gradle-ci?style=flat-square)](https://github.com/WakelessSloth56/ahutils-mod/actions)
&nbsp;
![Minecraft](https://img.shields.io/static/v1?label=Minecraft&message=1.18.1&color=00aa00&style=flat-square)
[![Forge](https://img.shields.io/static/v1?label=Forge&message=39.0.8&color=e04e14&logo=Conda-Forge&style=flat-square)](http://files.minecraftforge.net/net/minecraftforge/forge/index_1.18.1.html)
[![AdoptiumOpenJDK](https://img.shields.io/static/v1?label=AdoptiumOpenJDK&message=17.0.1%2B12&color=brightgreen&logo=java&style=flat-square)](https://adoptium.net/?variant=openjdk17&jvmVariant=hotspot)
[![Gradle](https://img.shields.io/static/v1?label=Gradle&message=7.3&color=brightgreen&logo=gradle&style=flat-square)](https://docs.gradle.org/7.3/release-notes.html)

</div>

## For Developers

There are two ways to use this mod in your workspace:

### GitHub Package (Recommended)

Add the following to your `build.gradle`:

```groovy
repositories {
    maven {
        url "https://maven.pkg.github.com/wakelesssloth56/ahutils-mod"
        credentials {
            username = "<GITHUB_USERNAME>"
            password = "<GITHUB_TOKEN>"
        }
    }
}

dependencies {
    compileOnly "org.auioc.mods.ahutils:ahutils-<MINECRAFT_VERSION>:<AHUTILS_VERSION>:forgelib"
    runtimeOnly "org.auioc.mods.ahutils:ahutils-<MINECRAFT_VERSION>:<AHUTILS_VERSION>"
}
```

#### Notices

1. Mod version can be found in the [Packages](https://github.com/WakelessSloth56/ahutils-mod/packages/) of this repository.
2. You must provide a valid GitHub username and token to access the GitHub Packages.
    - See [official documentation](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-gradle-registry#using-a-published-package) for more information.

### Local JAR File

If you do not want to or can not use the GitHub Packages:

1. Download the mod jar and forgelib jar of the version you want from [Releases](https://github.com/WakelessSloth56/ahutils-mod/releases), then put them into `libs` folder.

2. Add the following to your `build.gradle`:

    ```groovy
    dependencies {
        compileOnly files("libs/ahutils-<VERSION>-forgelib.jar")
        runtimeOnly files("libs/ahutils-<VERSION>.jar")
    }
    ```

## Documentation

- `zh-CN`: <https://wiki.auioc.com/view/Minecraft:Mod/AHUtils>

## Maintainers

- [@WakelessSloth56](https://github.com/WakelessSloth56)

## Credits

- [AUIOC](https://www.auioc.com)
- [bgxd9592](https://github.com/bgxd9592)
- [HaruhiFanClub](https://github.com/HaruhiFanClub)

## Contributing

Any type of contribution is welcome, here are some examples of how you may contribute to this project:

- Submit [issues](https://github.com/WakelessSloth56/ahutils-mod/issues) to report bugs or ask questions.
- Propose [pull requests](https://github.com/WakelessSloth56/ahutils-mod/pulls) to improve our code.

## License

AHUtils is licensed under the **GNU General Public License v3.0**.
Full license is in [LICENSE](/LICENSE) file.
