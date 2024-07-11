Flash Light dimming utilty for Redmi Note 12 Pro Plus
**Requires Root**

Features:
- Dark Mode
- Mostly Works
- Dimms your flashlight

Mostly ChatGPT'd together in a few minutes but it does what it's supposed to do

Note that it has only been tested only Redmi Note 12 Pro Plus but might work on other devices. It just does `echo BRIGHTNESS_VALUE > /sys/class/leds/torch-light0/brightness`, cou can change the file path to whatever you want in the MainActivity to potentially support other devices.

Add your own devices:
In the Cis utilty you can dim the flashlight, then just connect you PC and run `adb logcat | grep CitUtils` to find the filepath
