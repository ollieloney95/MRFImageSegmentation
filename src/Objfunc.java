import java.io.*;

public class Objfunc implements Objfun
{
	/*
	public double evalObjfun(double x[]){
		return (100*(x[1]-x[0]*x[0])*(x[1]-x[0]*x[0])+(1.0-x[0])*(1.0-x[0]));
	}
	*/

	public double evalObjfun(double x[]){
		float[] fx = new float[x.length];
		for(int i=0;i<x.length;i++){
			fx[i] = (float)x[i];
		}
		return ParameterEstimation.LogPseudoLikelihood(fx)*ParameterEstimation.LogPseudoLikelihood(fx);
	}

}