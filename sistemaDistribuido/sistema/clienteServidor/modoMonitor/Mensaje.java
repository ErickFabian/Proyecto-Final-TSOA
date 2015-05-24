/*Morales Guzman Jose Eduardo
 *Codigo: 207452411
 *Seccion D03
 *Practica  5
 */


package sistemaDistribuido.sistema.clienteServidor.modoMonitor;

public class Mensaje {
	
	private byte[] mensaje;
	private int intentos;
	private ParMaquinaProceso pmp;
	
	
	public Mensaje(byte[] mensaje,ParMaquinaProceso pmp){
		this.pmp = pmp;
		this.mensaje = mensaje;
	}
	
	public ParMaquinaProceso dameParMaquinaProceso(){
		return pmp;
	}
	
	public int dameIntentos(){
		return intentos;
	}
	
	public void incrementaIntentos(){
		intentos += 1;
	}
	
	public byte[] dameMensaje(){
		return mensaje;
	}

}
