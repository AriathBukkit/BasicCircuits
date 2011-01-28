BasicCircuits 0.7
==================

The basic circuit package for [RedstoneChips](http://eisental.github.com/RedstoneChips).

__For much more information, visit the [circuitdocs](http://eisental.github.com/RedstoneChips/circuitdocs).__

Installation
-------------
* If you're updating from a previous version, delete any previously installed RedstoneChips and BasicCircuits jar files and rename your <craftbukkit>/plugins/RedstoneChips-XX folder to RedstoneChips-0.7 (or delete it to remove previous settings).
* Download the [RedsoneChips-0.7](http://eisental.github.com/eisental/RedstoneChips/RedstoneChips-0.7.jar) jar file.
* Download the [BasicCircuits-0.7](http://eisental.github.com/eisental/BasicCircuits/BasicCircuits-0.7.jar) jar file.
* Copy the downloaded jar files into the plugins folder of your craftbukkit installation, keeping their original filenames.



Changelog
---------
#### 0.7 (27/01/11)
* iptransmitter and ipreceiver are disabled for the time being.
* New synth circuit for controlling noteblocks.
* Support for 1-bit pixel circuits.
* New counter sign arguments, min, max and direction.
* Added debug messages to counter.
* Changes to pixel, synth, and print to support multiple interface blocks.
* encoder now only requires that the number of inputs be less than or equal to the maximum number that can be represented by its outputs.
* clock is now limited to a minimum interval of 200ms.
* fixed some bugs and added debug messages to pisoregister and receiver.

#### 0.6 (24/01/11)
* new [iptransmitter](/eisental/BasicCircuits/wiki/Iptransmitter) and [ipreceiver](/eisental/BasicCircuits/wiki/Ipreceiver) circuits for your inter-planetary communication needs.
* new [pulse](/eisental/BasicCircuits/wiki/Pulse) circuit and a [not](/eisental/BasicCircuits/wiki/Not) gate circuit.
* [clock](/eisental/BasicCircuits/wiki/Clock) circuit now supports variable pulse widths.
* added send input pin to [transmitter](/eisental/BasicCircuits/wiki/Transmitter) circuit (thanks RustyDagger).
* fixed a bug in [shiftregister](/eisental/BasicCircuits/wiki/Shiftregister).
* new command /redchips-channels lists currently used broadcast channels.

#### 0.4 (22/01/11)
* NEW [pixel](/eisental/BasicCircuits/wiki/Pixel) circuit using colored wool as display pixels.
* print must have at least 2 inputs now.
* counter must have at least 1 input now.
* clock circuit is much more stable.


#### 0.2 (20/01/11)
* fixed a bug in decoder circuit.
* removed unnecessary log messages.

