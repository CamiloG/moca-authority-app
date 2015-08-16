# AuthorityApp
App for the Election Authorities, which is part of the [*MoCa QR*](https://github.com/CamiloG/moca_qr) Voting System project.

Android app for the authorities to store their share of the private key and decrypt the value present on the Bulletin Board server at the end of the election.

## Files
1. **MainActivity.java**:

2. **ConfigurationActivity.java**:

3. **DecryptActivity.java**:

4. **MultipliedBallot.java**:

### Minimum Requirements
### Hardware

### Apps installed

## How to Use
* Make sure your device satisfy the minimum requirements described above.
* Install the .apk file, which can be downloaded from [here](https://github.com/CamiloG/moca_qr/blob/master/Authority_Apps/authorityApp.apk?raw=true).

### Configuration of the Bulletin Board
* First of all you have to configure the root address for the Bulletin Board server. Select 'Configure Bulletin Board address' and introduce the address.
* The address is now shown on the main window of the application.

### Configuration of the Private Share
* Now the authority needs to record her share of the private key on the application, for that select 'Configure Private Share'.
* Select the location of the private share of the authority (recommended to be on an external storage recorded directly from [AuthKeyGenerator](http://www.github.com/CamiloG/AuthKeyGenerator)).
* Now is shown on the main window that the private share is already configured and working.

### Decrypting Process
* At the end of the election, select 'Generate Partial Decryption'.
* The program will download the multiplication of the ballots present on the Bulletin Board server.
* Then, will decrypt this value using the private share configured previously.
* Finally, the program will upload this partial decryption to the Bulletin Board server, finishing the application.
