You attach an electronic nanomesh tattoo to the girl’s skin.
In order to get correct readings from the interface between her skin and your electronics, you’ll need to descramble the data from all 14 channels.
Each channel is a data stream that uses a big-endian 8-bit encoding. Every byte in the channel points to another byte in the stream, i.e. the value of the byte is the offset in bytes from the beginning.
The channels can contain invalid bytes, which means following their pointer would cause an overflow off the stream.
Start reading data from the beginning of the channel, and ignore any invalid bytes until you encounter the first valid byte. After this, follow valid byte pointers until you reach another invalid byte.
This invalid byte contributes one character to the password. Repeat this process for all 14 channels to read the complete password.