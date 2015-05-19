package sistemaDistribuido.sistema.rpc.modoUsuario;

import java.util.Arrays;
import sistemaDistribuido.sistema.rpc.modoUsuario.Libreria;
import sistemaDistribuido.util.Escribano;

/** Fabian Garcia Erick Alfonso
*  Practica 3
*  Desarrollo de metodos abstractos de Clase padre Libreria para funcionamiento de RPC
*/
public class LibreriaServidor extends Libreria{

    /**
     * 
     */
    public LibreriaServidor(Escribano esc){
            super(esc);
    }

    protected void factorial(){
        imprimeln("Sacando parametros de Pila");
        Integer n = (Integer) pila.pop();
        int numero = n.intValue();
        imprimeln("Realizando Servicio Factorial");
        int resultado=1; 
        for(int i=numero;i>1;i--){ 
            resultado=resultado*i;
        }
        pila.push(new Integer(resultado));
    }

    protected void cuadrado() {
        imprimeln("Sacando parametros de Pila");
        Integer n = (Integer) pila.pop();
        int numero = n.intValue();
        imprimeln("Realizando Servicio Cuadrado");
        int resultado = numero * numero;
        pila.push(new Integer(resultado));
    }
    
    protected void moda() {
        imprimeln("Sacando parametros de Pila");
        Object[] moda_numbers = pila.toArray();
        pila.clear();
        Integer[] integerArray = Arrays.copyOf(moda_numbers, moda_numbers.length, Integer[].class);
        imprimeln("Realizando Servicio Moda");
        int resultado = obtener_moda(integerArray);
        pila.push(new Integer(resultado));
    }
    
    protected void sumatoria() {
        imprimeln("Sacando parametros de Pila");
        Object[] sum_numbers = pila.toArray();
        pila.clear();
        Integer[] integerArray = Arrays.copyOf(sum_numbers, sum_numbers.length, Integer[].class);
        imprimeln("Realizando Servicio Sumatoria");
        int resultado = obtener_sumatoria(integerArray);
        pila.push(new Integer(resultado));
    }
    
    private static int obtener_moda(Integer[] numbers){
        int counter = 0, max_actual, maximo, contador_maximo = -1;
        maximo = max_actual = numbers[0];
        for (int item : numbers)
        {
            if (max_actual == item){
                counter++;
            } else {
                if (counter > contador_maximo){
                    contador_maximo = counter;
                    maximo = max_actual;
                }
                counter = 0;
                max_actual = item;
            }
        }
        if (counter > contador_maximo){
            maximo = max_actual;
        }

        return maximo;
    }
    
    private static int obtener_sumatoria(Integer[] numbers){
        int sum = 0;
        for (int item: numbers){
            sum += item;
        }
        return sum;
    }
}