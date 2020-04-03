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