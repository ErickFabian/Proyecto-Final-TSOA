package sistemaDistribuido.sistema.clienteServidor.modoUsuario;


import sistemaDistribuido.sistema.clienteServidor.modoMonitor.ConectorDNS;
import sistemaDistribuido.sistema.rpc.modoUsuario.Datos;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;
import sistemaDistribuido.util.Escribano;
import sistemaDistribuido.util.Pausador;

/** Fabian Garcia Erick Alfonso
 * Practica 5
 * Realizo Registro y desregistro en interfaz del servidor de NOmbres
 */
public class ProcesoServidor extends Proceso{

    /**
     * 
     */
    public ProcesoServidor(Escribano esc){
        super(esc);
        start();
    }

    /**
     * 
     */
    public void run(){
            imprimeln("Proceso servidor en ejecucion.");
            byte[] solServidor=new byte[1024];
            byte[] respServidor=new byte[1024];
            
            Datos asa;
            int idUnico;
            imprimeln("El servidor se esta registrando en Servidor de Nombres");
            
            try 
            {
                asa = new Datos(Nucleo.dameIdProceso(), InetAddress.getLocalHost().getHostAddress());
                
            } catch (UnknownHostException ex) {
                asa = null;
                imprimeln("No se puede obtener ip local");
                Logger.getLogger(ProcesoServidor.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            idUnico = ConectorDNS.exportarInterfaz("Server Erick", "1.0", asa);
            
            
            while(continuar()){
                imprimeln("Invocando receive");
                Nucleo.receive(dameID(),solServidor);
                imprimeln("Procesando peticion recibida del cliente");
                unpack_message(solServidor);
                imprimeln("Generando mensaje a ser enviado, llenando los campos necesarios");
                pack_response(solServidor, respServidor);
                Pausador.pausa(1000);  //sin esta l�nea es posible que Servidor solicite send antes que Cliente solicite receive
                imprimeln("Señalamiento al nucleo para envio de mensaje");
                Nucleo.send(merge_bytes_int(solServidor),respServidor);
                imprimeln("Respuesta de servidor enviada");
            }
            ConectorDNS.deregistrarInterfaz("Server Erick", "1.0", idUnico);
    }
        
        public void unpack_message(byte[] solServidor){
            do_action(get_cod_op_from_request(solServidor), solServidor);
        }
        
        public void pack_response(byte[] solServidor, byte[] respServidor){
            pack_id_into_response(respServidor);
            pack_client_id_into_response(respServidor,solServidor);
            pack_message_into_response(respServidor, solServidor);
        }
        
        public void pack_id_into_response(byte[] respServidor){
            respServidor[0] = (byte) (dameID()>>>24);
            respServidor[1] = (byte) (dameID()>>>16);
            respServidor[2] = (byte) (dameID()>>>8);
            respServidor[3] = (byte) (dameID());
        }
        
        public void pack_client_id_into_response(byte[] respServidor, byte[] solServidor){
            byte[] arr2 = Arrays.copyOfRange(solServidor, 0, 4);
            int client_id = merge_bytes_int(arr2);
            respServidor[4] = (byte) (client_id >>> 24);
            respServidor[5] = (byte) (client_id >>> 16);
            respServidor[6] = (byte) (client_id >>> 8);
            respServidor[7] = (byte) (client_id);
        }
        
        public void pack_message_into_response(byte[] respServidor, byte[] solServidor){
            String message = unpack_string(solServidor);
            message+= "CODOP:"+get_cod_op_from_request(solServidor);
            respServidor[8] = (byte) message.length();
            byte[] aux = message.getBytes();
            System.arraycopy(aux, 0, respServidor, 9, aux.length);
        }
        
        public short get_cod_op_from_request(byte[] solServidor){
            return merge_bytes_short(solServidor[8], solServidor[9]);
        }
        
        public String unpack_string(byte[] solServidor){
            byte message_length = solServidor[10];
            byte[] aux = new byte[message_length];
            int i;
            int j = 0;
            for(i = 11; i < message_length + 11; i++){
                aux[j] = solServidor[i];
                j++;
            }
            return new String(aux);
        }
        
        public void do_action(short cod_op, byte[] solServidor){
            switch(cod_op){
                case 1: create_file(unpack_string(solServidor));
                        break;
                case 2: delete_file(unpack_string(solServidor));
                        break;
                case 3: read_file(unpack_string(solServidor));
                        break;
                case 4: write_to_file(unpack_string(solServidor));
                        break;
            }
        }
        
        /** Creacion de Archivo
        *   Recibe un nombre de archivo y lo crea en la carpeta del proyecto
        */
        public void create_file(String filename){
            try 
            {
          File file = new File(filename);
 
          if (file.createNewFile()){
            imprimeln("Archivo Creado con nombre: "+filename);
          }
              else
              {
            imprimeln("Archivo Ya existe(operacion Crear): "+filename);
          }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        /** Eliminacion de Archivo
        *   Recibe un nombre de archivo y lo elimina de la carpeta del proyecto
        */
        public void delete_file(String filename){  
            try
            {
            File file = new File(filename);
 
            if(file.delete()){
                    imprimeln("Archivo "+filename+" Borrado");
            }
                else
                {
                    imprimeln("Archivo "+filename+" No Borrado");
            }
 
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        /** Lectura de Archivo
        *   Recibe un nombre de archivo y lee el contenido
        */
        public void read_file(String filename){
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(filename));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ProcesoServidor.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                StringBuilder sb = new StringBuilder();
                String line = null;
                try {
                    line = br.readLine();
                } catch (IOException ex) {
                    Logger.getLogger(ProcesoServidor.class.getName()).log(Level.SEVERE, null, ex);
                }

                while (line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    try {
                        line = br.readLine();
                    } catch (IOException ex) {
                        Logger.getLogger(ProcesoServidor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                String everything = sb.toString();
                imprimeln("Contenido del archivo: "+filename+":"+everything);
            } finally {
                try {
                    br.close();
                } catch (IOException ex) {
                    Logger.getLogger(ProcesoServidor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
           
        }
        
        /** Escritura en Archivo
        *   Recibe un nombre de archivo y escribe una cadena separada por @ siendo la primera parte el nombre del archivo y la segunda el contenido a escribir
        */
        public void write_to_file(String message){
            String[] parts = message.split("@");
            String filename = parts[0];
            String content = parts[1];
            try 
            {
                File file = new File(filename);

                // if file doesnt exists, then create it
                if (!file.exists()) {
                        file.createNewFile();
                }

                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(content);
                bw.close();

                imprimeln("Escritura en archivo"+filename+"correcta de los datos:"+content);

            } catch (IOException e) {
                    e.printStackTrace();
            }
        }
        
        public short merge_bytes_short(byte b1, byte b2) {
            return (short) ((b1 << 8) | (b2 & 0xFF));
        }
        
        public int merge_bytes_int(byte[] bytes){
             int aux = 0;
             for (int i=0; i<4 && i<bytes.length; i++) {
                aux <<= 8;
                aux |= (int)bytes[i] & 0xFF;
            }
            return aux;
        }
}
