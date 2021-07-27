
package vhoster;

import java.util.Scanner;

/**
 * Crea in automatico un vhost su xaamp e aggiorna il file Windows Hosts.
 * Prima di ogni modifica, il file host viene preventivamente salvato nel file
 * hosts-bk
 * 
 * Comandi:
 * - GET                : restisuisce tutti i vhost configurati sul file hosts
 * - ADD                : inserisce un nuovo host inserendo l'url
 * - DEL                : cancella l'host sul file hosts (non cancella i file 
 *                        che compongono il sito)
 * - RESTORE            : ripristina la versione del file hosts precedente
 * - ?                  : lista dei comandi
 * - EXIT               : ESCI
 * 
 * Comandi non ancora disponibili:
 * - DISABLE            : Disabilita host con # ma non lo cancella
 * - ENABLE             : Abilita host disabilitato con #
 * 
 * @author Simone Vannucci
 * @version 30.04.2021
 */
public class Vhoster {
    
    /**
     * Chiede una stringa all'utente
     * @return stringa
     */
    public static String askUser() {
        
        Scanner in = new Scanner(System.in);
        return in.nextLine().trim();

    }
    
    /**
     * Stampa tutti gli host configurati sulla macchina
     * @param h oggetto Host
     */
    public static void printAllHosts(Host h){
        
        String[][] sites = h.getAllHosts();
        
        for (int i = 0; i < sites.length; i++) {
            System.out.printf("- %-15s %-30s %s\n",sites[i][Host.COLIP], sites[i][Host.COLURL], sites[i][Host.COLPATH]);
        }  
        
    }
    
    /**
     * Stampa i comandi disponibili
     */
    public static void printHelp(){
        
        final short COMMAND = 0;
        final short DESCRIPTION = 1;
        final short PATH = 2;
        String[][] cmd = {
            {"GET", "restisuisce tutti i vhost configurati sul file hosts"},
            {"ADD", "inserisce un nuovo host inserendo l'url e il path in cui verrà inserito il sito"},
            {"DEL", "cancella l'host sul file hosts (non cancella i file che compongono il sito)"},
            {"RESTORE", "ripristina la versione del file hosts precedente"},
            {"EXIT", "Uscita"}            
        };
        
        for (int i = 0; i < cmd.length; i++) {
            System.out.printf("- %-20s %s\n",cmd[i][COMMAND],cmd[i][DESCRIPTION] );            
        }
    
    }
    
    /**
     * Stampa messaggio di errore
     */
    public static void printError(){
        
        System.out.println("Comando non valido");
    
    }
    
    /**
     * Stampa messaggio di cancellazione comando
     */
    public static void printAbort(){
    
        System.out.println("Comando annullato");
    
    }
    
    /**
     * Aggiunge un sito agli host
     * Il metodo è pensato per permettere in un secondo momento di aggiungere 
     * la feature per inserire tutti i comendi in una sola linea
     * @param str informazioni inserite dell'utente (url)
     * @param h oggetto Host
     */
    public static void addSite(String str, Host h){
    
        String url = "", path = "", rootPath = h.getPath('s');
        boolean isExisting = false;
        
        if ( str.length() < 4 ){
            System.out.print("Inserisci url > ");
            url = askUser().toLowerCase();
        }
        
        path = rootPath + "\\" + url;        
        System.out.println("Stai per aggiungere il sito con i seguenti dati:");
        System.out.println("Sito: " +  url);
        System.out.println("Folder: " + path);
        System.out.print("Vuoi confermare [s = si, n = no] > ");
        String cmd = askUser().toLowerCase();
        
        if (h.hasHosts(url)){
            System.out.println("Sito già presente, cancellarlo o cambiare "
                               + "nome di dominio");
            isExisting = true;
        }
        
        if (cmd.charAt(0) == 's' && isExisting == false){
            h.addHost(url, path);
        } else {
            printAbort();
        }
       
    }
    
    /**
     * Cancella un sito agli host
     * Il metodo è pensato per permettere in un secondo momento di aggiungere 
     * la feature per inserire tutti i comendi in una sola linea
     * @param str informazioni inserite dell'utente (url)
     * @param h oggetto Host
     */
    public static void delSite(String str, Host h){
        
        String url = "";
    
        if ( str.length() < 4 ){
            System.out.print("Inserisci url > ");
            url = askUser().toLowerCase();
        }
        
        System.out.println("Stai per cancellare il seguente sito:");
        System.out.printf("- %-30s\n", url);
        System.out.print("Vuoi confermare [s = si, n = no] > ");
        String cmd = askUser().toLowerCase();

        if (cmd.charAt(0) == 's'){
            h.delHost(url);
        } else {
            printAbort();
        }
    
    }
    
    /**
     * Permette di ripristinare l'ultimo salvataggio dei file host
     * @param h oggetto Host
     */
    public static void restoreBackup(Host h){
    
        System.out.println("Stai per per ripristinare la versione precedente "
                           + "della configurazione host.");
        System.out.print("Vuoi confermare? [s = si, n = no] > ");
        String cmd = askUser().toLowerCase();

        if (cmd.charAt(0) == 's'){
            h.restoreBackeupHost();
        } else {
            printAbort();
        }        
    
    }

    public static void main(String[] args) {
        
        Boolean exit = false;
        Host host = new Host();
        
        do{          
            System.out.print("Inserisci comando o ? > ");
            String in = askUser().toUpperCase();           
            int strlenght = in.length();           
            if( strlenght == 1 && in.charAt(0) == '?'){
                printHelp();
            } else if( strlenght >= 3){
                if(in.equals("EXIT")){
                    exit = true;
                } else if(in.equals("GET")){
                   printAllHosts(host);
                } else if(in.substring(0, 3).equals("ADD")){
                    addSite(in, host);
                } else if(in.substring(0, 3).equals("DEL")){
                    delSite(in, host);
                } else if(in.equals("RESTORE")){
                    restoreBackup(host);
                }else {
                    printError();
                }
            } else {
                printError();
            }              
        } while(!exit);
                
    }
    
}
