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

1. Caricare il codice sorgente sugli Arduino master e slave.

2. Eseguire l'applicazione Java (App.java) sul computer.

3. Selezionare l'aula specifica e i banchi che si desidera controllare.

4. Utilizzare i pulsanti per alzare, abbassare o fermare i banchi selezionati.

5. Assicurarsi che la comunicazione con gli Arduino sia stabilita prima di iniziare a utilizzare l'applicazione.

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

