# Projekt Raamatukogu (now with GUI)

Autorid: René Piik ja Sander Roosalu

## Kirjeldus

Programm võimaldab luua ja hallata isiklikku raamatukogude kataloogi. Programmi mõte on selle lihtsuses: raamatute nägemiseks, lisamiseks ja muutmiseks pole tarvis internetiühendust ega kasutada suuri ja keerulisi kasutajaliideseid. Programm on pigem väike (<500kB; on disk <1MB) ja kiire.

## Klassid ja paketid
### com.sarec
Vaikepakett, kus hoitakse kõiki programmi tööks olevaid faile.

#### ConsoleInterface
_Singleton_ klass, mille kaudu kasutaja programmiga suhtleb. Siin paikneb kogu programmi loogika, mis seondub info käitlemisega. Klassi isend salvestab kasutaja sisestatud info ja programmi sulgemisel kirjutab selle failidesse.

#### Console
Jäänud klass projekti 1. osast. 2. osas sellega ei tegeleta, aga on jäänud.
Meetodis toimus kasutaja sisendi lugemine ja tõlgendamine.

#### Main
Siin luuakse klassi ConsoleInterface isend, mille kaudu suhtleb kasutaja programmiga. 

#### Startup
Klass, mille meetodit Initialize() jooksutatakse ühe korra uue _ConsoleInterface_ tüüpi objekti loomisel. Tagastab ./libs kausta salvestatud raamatukogude info.

#### Vars
Klass, kus hoitakse programmi töös kasutatavate erinevate muutujate andmeid. 

### com.sarec.components
#### Book
Raamatu klass, hoiustab raamatu isendiga seotud informatsiooni.

#### ISBN
Väike klass selleks, et uute raamatute info sisestamisel saaks kontrollida ISBN koodi õigsust.

#### Library
Raamatukogu klass, hoiustab _Book_ tüüpi objekte. Meetod Library.save() salvestab vastava raamatukogu info samanimelisse .csv faili ./libs/ kaustas.

#### NewLibraryStage
Klass selleks, et luua aken uue raamatukogu loomiseks. Tõstetud välja MainController.java-st, et tuua sinna selgust juurde.

#### PrimaryButton
Klass, mis extendib Button-it et seda saaks stiliseerida.

#### SecondaryButton
Sama eesmärk, mis PrimaryButton-il.

#### OperationType
Enum, mis hoiab võimalikke ConsoleInterface-i operatisoonitüüpe.

#### Status
Enum, mis hoiab võimalikke Book-objekti staatuseid.

### com.sarec.controllers
#### UpdateBookController
Klass, mis tegeleb raamatu uuendamise aknaga. Loodud, et MainController.java-sse selgust tuua.

#### MainController
Programmi liha.
Klass, mis sisaldab MainController constructorit ning suur hulk meetodeid, mis loovad erinevaid aknaid ja töötlevad akendes olevat informatsiooni.

### com.sarec.resources
Pakett sisaldab faile, et kasutajale nähtav osa ilus välja näeks.



## Protsess ja panus
Pärast projekti 1. osa valmimist jaotus töö laias laastus kaheks:
1. Suurema funktsionaalsuse lisamine (Back-end) ja mõnede iluvigade parandamine (Sander)
2. Väljanägemise kujundamine FXML-ga (Front-end) ja vajalikud klassid, et kood töötaks (René)

Funktsionaalsuse jaoks oli juba varasemalt loodud meetodid, mida pidi modifitseerima ja teistes kohtades kasutama erinevate ActionEventidega. 
Algse visiooni väljanägemisest joonistas René keskkonnas Figma.

Kui mõlema osapoole tööd said valmis oma harudes, _merge_-sime harud ja tegime väikseid muudatusi edaspidi _master_ harusse. Suhtlesime teineteisega jooksvalt, kui tekkis uusi ideid ja probleeme koodiga, ning kohati ka jagasime tööülesandeid kordamööda.


## Mured
Programm töötab hetkel Windowsi peal, töökindlus teistel operatisoonisüsteemidel ei ole teada ja oleks vaja testida.


## Hinnang
Tööga saime hästi hakkama, ühtegi ületamatut muret ei tekkinud. Programmil on võimalus veel rohkem funktsionaalsust juurde lisada, kuna see ei oleks keeruline.


## Testimine
Testimine toimus jooksvalt. Uute meetodite kirjutamisel testisime, kuidas need eri sorti sisendite puhul käituvad ja kas soovitud funktsionaalsus on tagatud. Pärast uue koodi kirjutamist lugesime ja testisime ka teineteise koodi. Viimase kogu projekti funktsionaalsuse testimise tegi läbi Sander.
