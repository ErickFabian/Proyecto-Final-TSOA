package sistemaDistribuido.sistema.clienteServidor.modoMonitor;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.ConectorDNS;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import sistemaDistribuido.sistema.clienteServidor.modoMonitor.MicroNucleoBase;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;

/** Fabian Garcia Erick Alfonso
 * Practica 5
 * Implementacion de send verdadero con parametro String para busqueda en el servidor de nombres
 */
public final class MicroNucleo extends MicroNucleoBase{
  private static MicroNucleo nucleo=new MicroNucleo();
        Hashtable< Integer, ObjectForRequest > TablaEmision= new Hashtable<Integer, ObjectForRequest>();
        Hashtable< Integer, ObjectForRequest > TablaRecepcion = new Hashtable<Integer, ObjectForRequest>();

  /**
   * 
   */
  private MicroNucleo(){
  }

  /**
   * 
   */
  public final static MicroNucleo obtenerMicroNucleo(){
    return nucleo;
  }

  /*---Metodos para probar el paso de mensajes entre los procesos cliente y servidor en ausencia de datagramas.
    Esta es una forma incorrecta de programacion "por uso de variables globales" (en este caso atributos de clase)
    ya que, para empezar, no se usan ambos parametros en los metodos y fallaria si dos procesos invocaran
    simultaneamente a receiveFalso() al reescriir el atributo mensaje---*/
  byte[] mensaje;

  public void sendFalso(int dest,byte[] message){
    System.arraycopy(message,0,mensaje,0,message.length);
    notificarHilos();  //Reanuda la ejecucion del proceso que haya invocado a receiveFalso()
  }

  public void receiveFalso(int addr,byte[] message){
    mensaje=message;
    suspenderProceso();
  }
  /*---------------------------------------------------------*/

  /**
   * 
   */
  protected boolean iniciarModulos(){
    return true;
  }

  /**
   * 
   */
        
  protected void sendVerdadero(int dest,byte[] message){
    try 
    {
        if(TablaEmision.containsKey(dest))
        {
            ObjectForRequest pmp = TablaEmision.get(dest);
            pack_request(super.dameIdProceso(),pmp.id,message);
        }
        else
        {
            ParMaquinaProceso pmp = dameDestinatarioDesdeInterfaz();
            pack_request(super.dameIdProceso(),pmp.dameID(),message);
        }
        
        DatagramPacket dp;
        dp = new DatagramPacket(message,message.length,InetAddress.getByName(pmp.dameIP()),this.damePuertoRecepcion());
        DatagramSocket socketEmision=this.dameSocketEmision();
        try {
            socketEmision.send(dp);
        } catch (IOException ex) {}
    } catch (UnknownHostException ex) {}            
  }

  public void pack_request(int origin, int server, byte[] message){
      pack_id_into_request(message);
      pack_server_id_into_request(message, server);
  }
  
  public void pack_id_into_request(byte[] paquete){
      int origen = super.dameIdProceso();
      
      paquete[0] = (byte) (origen >>> 24);
      paquete[1] = (byte) (origen >>>16);
      paquete[2] = (byte) (origen >>>8);
      paquete[3] = (byte) (origen);
  }
  
  public void pack_server_id_into_request(byte[] paquete,int server){
      paquete[4] = (byte) (server >>>24);
      paquete[5] = (byte) (server >>>16);
      paquete[6] = (byte) (server >>>8);
      paquete[7] = (byte) (server);
  }
        
  protected void receiveVerdadero(int addr,byte[] message){
    ObjectForRequest object= new ObjectForRequest(addr,"127.0.0.1",message);
    TablaRecepcion.put(object.id, object);
    suspenderProceso();
  }
        
  /**
   * Para el(la) encargad@ de direccionamiento por servidor de nombres en pr�ctica 5  
   */
  protected void sendVerdadero(String dest,byte[] message){
//      System.out.println("In MIcroNucleo Dest must be Server == "+dest);
//      System.out.println("Sanity Check message"+Arrays.toString(message));
    int id = ConectorDNS.importarInterfaz(dest);
    pack_request(super.dameIdProceso(),id,message);
    DatagramPacket dp = null;
    try {
        dp = new DatagramPacket(message,message.length,InetAddress.getByName(pmp.dameIP()),this.damePuertoRecepcion());
    } catch (UnknownHostException ex) {
        Logger.getLogger(MicroNucleo.class.getName()).log(Level.SEVERE, null, ex);
    }
    DatagramSocket socketEmision=this.dameSocketEmision();
    try {
        socketEmision.send(dp);
    } catch (IOException ex) {}
  }

  /**
   * Para el(la) encargad@ de primitivas sin bloqueo en pr�ctica 5
   */
  protected void sendNBVerdadero(int dest,byte[] message){
  }

  /**
   * Para el(la) encargad@ de primitivas sin bloqueo en pr�ctica 5
   */
  protected void receiveNBVerdadero(int addr,byte[] message){
  }

  /**
   * 
   */
  public void run(){
      byte[] buffer=new byte[1024];
      while(seguirEsperandoDatagramas())
      {
          try
          {
              DatagramSocket recepcion=dameSocketRecepcion();
              DatagramPacket dp=new DatagramPacket(buffer,buffer.length);

              try {
                  recepcion.receive(dp);
              } catch (IOException ex) {}
              int id_origin = merge_bytes_int(buffer, 0, 4);
              int id_server = merge_bytes_int(buffer, 4, 8);
              Proceso process = dameProcesoLocal(id_server);
              if(TablaRecepcion.containsKey(id_server))
              {
                ObjectForRequest object=new ObjectForRequest(id_origin,dp.getAddress().toString(),new byte[1024]);
                TablaEmision.put(object.id, object);
                System.arraycopy(dp.getData(),0,TablaRecepcion.get(id_server).message, 0,dp.getData().length);
                TablaRecepcion.remove(id_server);
                reanudarProceso(process);
              }
              else 
              {
                if(process_is_not_local(process))
                {
                  imprimeln("ADDRESS UNKNOWN");
                  System.out.println("Dimension desconocida");
                  sleep(600);
                  Proceso a = dameProcesoLocal(id_origin);
                  address_unknown_message(dp.getData());
                  System.arraycopy(dp.getData(),0,TablaRecepcion.get(id_origin).message, 0,dp.getData().length);
                  reanudarProceso(a);
                }
              }
              sleep(6);
          }catch(InterruptedException e){}
      }
  }
        
  public void address_unknown_message(byte []message){
      String error="Address Unknown";
      byte [] aux = error.getBytes();
      System.arraycopy(aux, 0, message, 11, aux.length);
  } 
  
  public boolean process_is_not_local(Proceso process)
  {
      return process == null;
  }
  
  public int merge_bytes_int(byte[] bytes, int start, int end){
      
      int aux = 0;
      for (int i=start; i<end && i<bytes.length; i++) {
          aux <<= 8;
          aux |= (int)bytes[i] & 0xFF;
      }
      return aux;
  }

   public int registrarTablaEmision(ParMaquinaProceso asa) {
       ObjectForRequest object=new ObjectForRequest(asa.dameID(),asa.dameIP(),new byte[1024]);
       TablaEmision.put(asa.dameID(), object);
       return asa.dameID();
   }
  
}

class ObjectForRequest {
    public int id;
    public String ip;
    public byte[] message= new byte[1024];
    
    public ObjectForRequest(int id,String ip, byte[] message){
        this.id=id;
        this.ip = ip;
        this.message = message;
    }
}