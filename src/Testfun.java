/**
* Program: Testfun.java
* Author: Michael F. Hutt
* http://www.mikehutt.com
* Mar. 17, 2011
* javac NMSimplex.java Testfun.java
* test function for nmsimplex
*
*/


import java.io.*;

public class Testfun
{
	public static void main(String[] args) throws IOException
	{
		double start[] = {0.5,1.0};
		int dim = 2;
		double eps = 1.0e-4;
		double scale = 1.0;
		int i;

		NMSimplex simplex = new NMSimplex(start,dim,eps,scale);

		for (i=0; i<dim; i++) {
			System.out.format("%f\n",start[i]);
		}
	}

}