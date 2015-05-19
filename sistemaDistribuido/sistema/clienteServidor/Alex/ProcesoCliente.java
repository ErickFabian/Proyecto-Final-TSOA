package sistemaDistribuido.sistema.clienteServidor.Alex;
//Martínez Rejín,Moisés Alexander
//207435363
//Taller de sistemas operativos sección D03
//Modificado para prácica  1,2 y 5
import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;
import sistemaDistribuido.util.Escribano;
/**
 * 
 */
public class ProcesoCliente extends Proceso{
        int m1;
        String mabu="";
	/**
	 * 
	 */
	public ProcesoCliente(Escribano esc){
		super(esc);
		start();
	}

	/**
	 * 
	 */
        public void Valores(int com,String datos )
        {
          mabu=datos;
          m1=com;
        }
	public void run(){
		int i,contador,tope;
        String respuesta="";
        imprimeln("Proceso cliente en ejecucion.");
		imprimeln("Esperando datos para continuar.");
		Nucleo.suspenderProceso();
		imprimeln("Inicio de proceso");
		byte[] solCliente=new byte[1024];
		byte[] respCliente=new byte[1024];
        imprimeln("Generando mensaje a ser enviado");
        imprimeln("llenando los campos necesarios");
        solCliente[0]=(byte)dameID();
        solCliente[8]=(byte)mabu.length();
        solCliente[10]=(byte)m1;
        System.arraycopy(mabu.getBytes(),0,solCliente,12,mabu.length());
		/*do
		 {*/
          imprimeln("SeÃ±alando al nÃºcleo para envÃ­o de mensaje");
		  Nucleo.send(248,solCliente);
          imprimeln("Invocano a receive");
		  Nucleo.receive(dameID(),respCliente);
		  /*if(((int)respCliente[1023])==-1)
		   imprimeln("Intentado de nuevo");
		 }while(((int)respCliente[1023])==-1);*/
		imprimeln("Procesando respuesta recibida del servidor");
		tope=(int)respCliente[8];
        for(i=0,contador=10;i<tope;i++,contador++)
         respuesta=respuesta+((char)respCliente[contador]);
        imprimeln("Resultado de la operacion: ");
         if(((int) respCliente[1023])!=1 && ((int) respCliente[1023])!=-1)
          imprimeln("AU");
         else
          imprimeln(respuesta);
	}
}
