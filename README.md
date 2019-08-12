# PhoneNumber-Encoding

Configuration Steps : 
1. Update the src/main/resources/application.properties with input file paths.
2. mvn clean install
3. java -jar target/number-encoding-0.0.1-SNAPSHOT.jar

----------------------------------------------------------------------------------------
Task description
=================

The following mapping from letters to digits is given:

E | J N Q | R W X | D S Y | F T | A M | C I V | B K U | L O P | G H Z </br>
e | j n q | r w x | d s y | f t | a m | c i v | b k u | l o p | g h z </br>
0 |   1   |   2   |   3   |  4  |  5  |   6   |   7   |   8   |   9 </br>

Only exactly each encoding that is possible from this dictionary and
that matches the phone number exactly shall be printed. Thus, possibly
nothing is printed at all. The words in the dictionary contain letters
(capital or small, but the difference is ignored in the sorting), dashes
- and double quotes " . For the encoding only the letters are used, but
the words must be printed in exactly the form given in the dictionary.
Leading non-letters do not occur in the dictionary.

Encodings of phone numbers can consist of a single word or of multiple
words separated by spaces. The encodings are built word by word from
left to right. If and only if at a particular point no word at all from
the dictionary can be inserted, a single digit from the phone number can
be copied to the encoding instead. Two subsequent digits are never
allowed, though. To put it differently: In a partial encoding that
currently covers k digits, digit k+1 is encoded by itself if and only if,
first, digit k was not encoded by a digit and, second, there is no word
in the dictionary that can be used in the encoding starting at digit k+1.

Program worka on a series of phone numbers; for each encoding
that it finds, it must print the phone number followed by a colon, a
single(!) space, and the encoding on one line; trailing spaces are not
allowed. All remaining ambiguities in this specification will be
resolved by the following example. (Still remaining ambiguities are
intended degrees of freedom.)

Sample dictionary:

an
blau
Bo"
Boot
bo"s
da
Fee
fern
Fest
fort
je
jemand
mir
Mix
Mixer
Name
neu
o"d
Ort
so
Tor
Torf
Wasser

Sample phone number list:

112
5624-82
4824
0721/608-4067
10/783--5
1078-913-5
381482
04824

Corresponding correct program output (on screen):
5624-82: mir Tor
5624-82: Mix Tor
4824: Torf
4824: fort
4824: Tor 4
10/783--5: neu o"d 5
10/783--5: je bo"s 5
10/783--5: je Bo" da
381482: so 1 Tor
04824: 0 Torf
04824: 0 fort
04824: 0 Tor 4
