# MaintenanceKit
### What is MaintenanceKit
MaintenanceKit is a simple framework to help with determining one of the following conditions:
1. If a server is down and in maintenance mode
2. If there is an app update available

### Supported Versions
* [iOS/macOS Version](https://github.com/uptech/maintenancekit)
* [Android Version](https://github.com/uptech/android-maintenance-kit)

### Publishing
- This library is published to Github Packages via [Gradle Mavenizer](https://github.com/chillbrodev/gradle-maven-plugin)

### JVM Target
```
kotlinOptions {
    jvmTarget = JavaVersion.VERSION_1_8.toString()
}
```

### Handling Updates
MaintenanceKit allows for a simple and convenient way to handle checking for app updates. You specify the latest version and the minimum functioning version of the app in the JSON file along with a build number. There are two ways of determining the version, and that is either by the version string (1.0.0) or the build number (22)

Simply call the check for updates function in the MaintenanceService class to check for updates for the current app.

### Handeling Maintenance Mode
Maintenance mode is as simple as calling a function and checking returned parameters. There are two types of maintenance. Offline and online maintenance. You can set the type inside of the JSON file.

## JSON Information

### Response Info
The JSOn response contains two optional root nodes .
1. *upgrade*
2. *maintenance*

*Upgrades:* Upgrades consist of an array of platforms. Currently only `iOS`, `macOS` and `Android` are supported, future versions may introduce additional platforms.

*Maintenance:* Maintenance is simple information regarding potential downtime or active downtime.

Each platform can contain a nullable `message` dictionary containing two keys, `title` and `body`. This can be used to help clarify to users what is happening in a maintenance release or new app update.

### JSON Example
```json
{
    "upgrade": {
        "platforms": [{
            "platform": "ios",
            "latest_version": "1.0.0",
            "latest_build_number": 21,
            "store_url": "https://...",
            "minimum_version": "1.0.0",
            "minimum_build_number": 21,
            "required_update": false,
            "show_version_info": false,
            "message": {
                "title": "Upgrade Title",
                "body": "Upgrade Description"
            }
        }]
    },
    "maintenance": {
        "active": true,
        "offline": true,
        "scheduled": false,
        "start_date": "ISO-8601 UTC Date",
        "end_date": "ISO-8601 UTC Date",
        "message": {
            "title": "Upgrade Title",
            "body": "Upgrade Description"
        }
    }
}

```

## License

`Android Maintenance Kit` is Copyright © 2017-2020 UpTech Works, LLC. It is free software, and
may be redistributed under the terms specified in the LICENSE file.

## About <img src="http://upte.ch/img/logo.png" width="180">

`Android Maintenance Kit` is maintained and funded by [UpTech Works, LLC][uptech], a
software product, design & development consultancy.

We love open source software. See [our other projects][community] or
[hire us][hire] to design, develop, and grow your product.

[community]: https://github.com/uptech
[hire]: http://upte.ch
[uptech]: http://upte.ch
[carthage]: https://github.com/Carthage/Carthage
[constraid]: https://github.com/uptech/Constraid
[crazyman]: https://www.shutterstock.com/image-vector/crazy-cartoon-man-straight-jacket-193143881
[Ron Leishman]: https://www.shutterstock.com/g/Ron+Leishman
[travisproject]: https://travis-ci.org/uptech/Constraid
[gitterroom]: https://gitter.im/uptech/Constraid?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge
[cartfile]: https://github.com/Carthage/Carthage#adding-frameworks-to-an-application
