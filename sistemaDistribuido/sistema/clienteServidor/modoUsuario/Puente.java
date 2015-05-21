
package sistemaDistribuido.sistema.clienteServidor.modoUsuario;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import sistemaDistribuido.sistema.clienteServidor.modoMonitor.ParMaquinaProceso;
import sistemaDistribuido.visual.rpc.DespleganteConexiones;

/**
 *
 * @author Fabian Garcia Erick Alfonso
 * Practica 5
 * Clase Nueva creada con funcionalidad de puente entre interfaz de servidor de nombres y su conector
 */
public class Puente{
    private DespleganteConexiones desplegante;
    private Hashtable<Integer,Servidor> conexiones;   //las llaves que provee DespleganteConexiones
    private Set<Integer> lista;


    /**
     * 
     */
    public Puente(DespleganteConexiones desplegante){
            this.desplegante=desplegante;
    }

    /**
     * Inicializar tablas en programa conector
     */
    public void inicializar(){
            conexiones=new Hashtable<Integer,Servidor>();
    }

    /**
     * Remueve tuplas visualizadas en la interfaz gr�fica registradas en tabla conexiones
     */
    public void removerConexiones(){
            Set<Integer> s=conexiones.keySet();
            Iterator<Integer> i=s.iterator();
            while(i.hasNext()){
                    desplegante.removerServidor((i.next()));
                    i.remove();
            }
    }

    /**
     * Al solicitar que se termine el proceso, por si se implementa como tal
     */
    public void terminar() {
            removerConexiones();
            desplegante.finalizar();
    }
       
    public int registro(String nombreServidor, String version, ParMaquinaProceso asa) {
        int id_unico = desplegante.agregarServidor(nombreServidor, version, asa.dameIP(),String.valueOf(asa.dameID()));
        Servidor server = new Servidor(asa.dameID(), asa.dameIP(), nombreServidor, version);
        conexiones.put(id_unico, server);
        return id_unico;
    }
    
    public boolean deregistrar(String nombreServidor, String version, int identificacionUnica) { 
        if(conexiones.containsKey(identificacionUnica))
        {
            conexiones.remove(identificacionUnica);
            desplegante.removerServidor(identificacionUnica);
            return true;
        }
        return false;
    }
    
    public ParMaquinaProceso busqueda(String nombreServidor) {
        ParMaquinaProceso [] servidores =new  ParMaquinaProceso [conexiones.size()];
        ParMaquinaProceso ser=null;
        Servidor server = null;
        int i=0;
        lista=conexiones.keySet();
        for(Integer key : lista){
            server= conexiones.get(key);
            if (server.dameNombreServidor().equalsIgnoreCase(nombreServidor))
            {
                //servidores[i] = server;
                //i++;
            	ser=server;
            }
        }
        /*Random rn = new Random();
        int random_n = rn.nextInt(servidores.length - 1 + 1);*/
        //return servidores[random_n];
        return ser;
    }
    
    class Servidor implements ParMaquinaProceso{
        int id;
        String ip;
        String nombreServidor;
        String version;

        public Servidor(int id, String ip, String nombreServidor, String version) {
            this.id = id;
            this.ip = ip;
            this.nombreServidor = nombreServidor;
            this.version = version;
        }
        
        @Override
        public int dameID() {
            return id;
        }
        
        @Override
        public String dameIP() {
            return ip;
        }        
        
        public String dameNombreServidor() {
            return nombreServidor;
        }
        
        public String dameVersion() {
            return version;
        }
    }
}

