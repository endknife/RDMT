# Telecomando per Controllo Banchi

Questo progetto Java implementa un'applicazione per il controllo di banchi tramite un'interfaccia grafica. Il telecomando è progettato per comunicare con due dispositivi Arduino (master e slave) per controllare il movimento dei banchi in un'aula specifica.

## Struttura del Progetto

Il progetto è diviso in tre file principali:

1. **App.java**: Questo file contiene l'applicazione principale, inclusi l'interfaccia utente, i componenti GUI e la gestione delle comunicazioni con gli Arduino.

2. **Communication.java**: Questo file gestisce la comunicazione tra l'applicazione Java e i dispositivi Arduino. Include metodi per l'invio di comandi ai dispositivi e l'inizializzazione della connessione seriale.

3. **Arduino Master e Slave**: Sono inclusi anche i codici sorgente per gli Arduino master e slave. Il master invia comandi all'Arduino slave, che controlla il movimento dei banchi in base ai comandi ricevuti.

## Funzionalità Principali

- L'applicazione offre un'interfaccia utente grafica che consente agli utenti di selezionare e controllare i banchi in un'aula specifica.

- Gli Arduino master e slave comunicano tramite radiofrequenza (ASK) per gestire il movimento dei banchi in risposta ai comandi inviati dall'applicazione.

- L'applicazione è in grado di selezionare singoli banchi, alzarli, abbassarli e fermarli simultaneamente.

## Utilizzo

Per utilizzare questo progetto, seguire questi passi:

### 1. Caricare il codice sorgente sugli Arduino master e slave.
- Dovrai modificare queste due linee di codice nel arudino slave
  ```c++
  const String idAula = "B112";
  const int banchi[] = {2,3,4};
  ```
- Prima di caricare i codici sugli Arduini dovrai installare e includere la libreria **RadioHead**. Troverai i link dentro al readme.txt
  
### 3. Eseguire l'applicazione Java ***remote.jar** sul computer.
- Se vuoi modificare una aula o crearne una nuvoa dovrai creare un nuovo progetto e inserire tutti i file dentro alla cartella /java/project_files/*. Una volta fatto ciò dovrai modificare queste due variabili nel file ***Communication.java***
  ```Java
  private final static String HEADER = "#B112#";
  private String serialNumber = "7573530363135131A122";
  ```
- Nota bene una volta modificato tutte queste variabili ti potrai creare il proprio .jar dovrai **buildare** il tuo artifact. Se il progetto è stato fatto con **intellij** basterà seguire questa [documentazione](https://www.jetbrains.com/help/idea/working-with-artifacts.html#deploy_artifact).
- Prima di buildare il tuo progetto dovrai importare come libreria esterna **JSerialPort**. I vari link li trovi nelle cartelle dentro al readme.txt
- Se non dovessi vedere le icone dovrai impostarle come ***resource root*** le varie cartelle con dentro i tuoi file, per poi aggiungere dentro a File/Project Structure/Artifacts una nuova cartella *Directory Content* selezionando la cartella stessa.

### 4. Selezionare l'aula specifica e i banchi che si desidera controllare.

### 5. Utilizzare i pulsanti per alzare, abbassare o fermare i banchi selezionati.

### 6. Assicurarsi che la comunicazione con gli Arduino sia stabilita prima di iniziare a utilizzare l'applicazione.

## Requisiti

- Java Development Kit (JDK)
- Libreria RadioHead (per la comunicazione radiofrequenza)
- Arduino IDE (per caricare il codice sugli Arduino)

## Autori

Questo progetto è stato sviluppato da [Nome dell'autore 1] e [Nome dell'autore 2].

## Licenza

Questo progetto è rilasciato con la seguente licenza: [Inserire il tipo di licenza].

## Contatti

Per domande o ulteriori informazioni sul progetto, è possibile contattare gli autori all'indirizzo email [email@example.com].

## Ringraziamenti

Gli autori desiderano ringraziare [Nome delle persone o organizzazioni] per il supporto e il contributo a questo progetto.

