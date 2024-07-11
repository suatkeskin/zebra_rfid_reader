# zebra_rfid_reader_platform_interface

A common platform interface for the [`zebra_rfid_reader`][1] plugin.

This interface allows platform-specific implementations of the `zebra_rfid_reader`
plugin, as well as the plugin itself, to ensure they are supporting the
same interface.

# Usage

To implement a new platform-specific implementation of `zebra_rfid_reader`, extend
[`ZebraRfidReaderPlatform`][2] with an implementation that performs the
platform-specific behavior, and when you register your plugin, set the default
`ZebraRfidReaderLoader` by calling the `ZebraRfidReaderPlatform.loader` setter.

# Note on breaking changes

Strongly prefer non-breaking changes (such as adding a method to the interface)
over breaking changes for this package.

See https://flutter.dev/go/platform-interface-breaking-changes for a discussion
on why a less-clean interface is preferable to a breaking change.

[1]: ../zebra_rfid_reader

[2]: lib/zebra_rfid_reader_platform_interface.dart