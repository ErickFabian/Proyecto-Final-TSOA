package sistemaDistribuido.sistema.rpc.modoUsuario;

import java.util.Arrays;
import java.util.Stack;
import sistemaDistribuido.util.Escribano;

/**
 * @author Fabian Garcia Erick Alfonso
 * Practica 3
 * codigo: 207465432
 * Modificaciones:
 *  Desarrollo de funciones para llamadas a RPC
 */
public abstract class Libreria{
    protected Stack pila = new Stack();
	private Escribano esc;

	/**
	 * 
	 */
	public Libreria(Escribano esc){
		this.esc=esc;
	}

	/**
	 * 
	 */
	protected void imprime(String s){
		esc.imprime(s);
	}

	/**
	 * 
	 */
	protected void imprimeln(String s){
		esc.imprimeln(s);
	}
        
        protected int cuadrado(int cuadrado) {
            imprimeln("Insertando Parametros en la Pila");  
            pila.push((Integer) cuadrado);
            cuadrado();
            Integer res = (Integer) pila.pop();
            return (res.intValue());
        }
        
        protected int factorial(int factorial) {
            imprimeln("Insertando Parametros en la Pila");  
            pila.push((Integer) factorial);
            factorial();
            Integer res = (Integer) pila.pop();
            return (res.intValue());
        }
        
        protected int moda(int[] moda) {
            imprimeln("Insertando Parametros en la Pila");
            for(int item : moda){
                pila.push(item);
            }
            moda();
            Integer res = (Integer) pila.pop();
            pila.clear();
            return (res.intValue());
        }

	public int sumatoria(int[] sum){
            imprimeln("Insertando Parametros en la Pila");  
            for(int item : sum){
                pila.push(item);
            }
            sumatoria();
            Integer res = (Integer) pila.pop();
            pila.clear();
            return (res.intValue());
	}

        protected abstract void cuadrado();
        protected abstract void factorial();
        protected abstract void moda();
        protected abstract void sumatoria();

        
}