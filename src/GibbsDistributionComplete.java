import java.util.List;
import java.util.Random;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.imageio.ImageIO;

public class GibbsDistributionComplete {
	static int[][] img = new int[100][100];
	public void main(String[] args) throws IOException{
		//randomiseImage();
		//isingModelExample();
		//ising(img);
		// run from command cd Desktop\buffs & ffmpeg -framerate 10 -i bufferedImage%03d.jpg video.webm
	}
	private static void makeVid() throws IOException{
		Runtime rt = Runtime.getRuntime();
		Process pr = rt.exec("ffmpeg -framerate 3 -i bufferedImage%03d.jpg video.webm");
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(pr.getInputStream()));
	}
	private static List<List<Integer[]>> cliqueList(int[][] image,int x,int y){
		Set<Set<Integer>> ps = getPowerSet(getNeighbours4(x, y, image).size()+1);
		List<Integer[]> originalList = getNeighbours4(x, y, image);
		Integer[] ii = new Integer[2];ii[0] = x;ii[1]=y;originalList.add(ii);
		List<List<Integer[]>> potentialCliques = locationPowerset(originalList,ps);
		List<List<Integer[]>> cliques = new ArrayList<List<Integer[]>>();
		for(List<Integer[]> cliqueToConsider : potentialCliques){
			boolean goodClique = true;
			for(Integer[] pos1 :cliqueToConsider){
				//goodClique=true;
				List<Integer[]> neighs = getNeighbours4(pos1[0], pos1[1], image);
				for(Integer[] pos2 :cliqueToConsider){
					if(pos2!=pos1 && !conts(neighs,pos2)){
						goodClique=false;
					}
				}
			}
			if(goodClique == true){
				cliques.add(cliqueToConsider);
			}
		}
		
		return cliques;
		//return potentialCliques;
	}
	private static boolean conts(List<Integer[]> list , Integer[] element){
		for(int i = 0; i<list.size();i++){
			if(list.get(i)[0] == element[0]){
				if(list.get(i)[1] == element[1]){
					return true;
				}
			}
		}
		return false;
	}
	private static boolean containsClique(List<List<Integer[]>> cliqueList , List<Integer[]> clique){
		for(List<Integer[]> cliqueToConsider : cliqueList){
			boolean containsPoints=true;
			for(Integer[] point : clique){
				if(!conts(cliqueToConsider,point)){
					containsPoints=false;
				}
			}
			for(Integer[] point : cliqueToConsider){
				if(!conts(clique,point)){
					containsPoints=false;
				}
			}
			if(containsPoints == true){
				return true;
			}
		}
		return false;
	}
	private static List<List<Integer[]>> locationPowerset(List<Integer[]> originalList,Set<Set<Integer>> ps){
		List<List<Integer[]>> outputList = new ArrayList<List<Integer[]>>();
		for(Set<Integer> set : ps){
			List<Integer[]> tmpList = new ArrayList<Integer[]>();
			for(int i : set){
				tmpList.add(originalList.get(i-1));
			}
			outputList.add(tmpList);
		}
		return outputList;
	}
	private static Set<Set<Integer>> getPowerSet(int len) {
		Set<Integer> dataset = new HashSet<Integer>();
		for(int i = 1;i<len+1;i++){
			dataset.add(i);
		}
		Set<Set<Integer>> totalSet = powerSet(dataset);
		Set<Set<Integer>> setToReturn = new HashSet<Set<Integer>>();
		for(Set<Integer> i : totalSet){
			if(i.size() >=1){
				setToReturn.add(i);
			}
		}
		return setToReturn;
	}
	private static Set<Set<Integer>> powerSet(Set<Integer> originalSet) {
        Set<Set<Integer>> sets = new HashSet<Set<Integer>>();
        if (originalSet.isEmpty()) {
            sets.add(new HashSet<Integer>());
            return sets;
        }
        List<Integer> list = new ArrayList<Integer>(originalSet);
        Integer head = list.get(0);
        Set<Integer> rest = new HashSet<Integer>(list.subList(1, list.size()));
        for (Set<Integer> set : powerSet(rest)) {
            Set<Integer> newSet = new HashSet<Integer>();
            newSet.add(head);
            newSet.addAll(set);
            sets.add(newSet);
            sets.add(set);
        }
        return sets;
    }
	private static float cliquePotential(List<Integer[]> clique,int[][] image){
		return 0f;
	}
	private static float calculateEnergyNoClique(int[][] image,List<List<Integer[]>> cliques){
		float z=0f;
		for(List<Integer[]> clique : cliques){
			z+=cliquePotential(clique,image);
		}
		System.out.println("Z:"+z);
		return z*0.1f;	
	}
	private static float getProbNoClique(int[][] image,List<List<Integer[]>> cliques){
		return((float)Math.exp(-1*calculateEnergyNoClique(image,cliques)));
    }
	public static List<List<Integer[]>> allCliques(int[][] image){
		List<List<Integer[]>> cliques = new ArrayList<List<Integer[]>>();
		for(int r=1;r<=image.length;r++){
			for(int c=1;c<=image[0].length;c++){
				cliques.addAll(cliqueList(image,c,r));
			}
		}
		return removeDuplicateCliques(cliques);
	}
	public static List<List<Integer[]>> allCliquesInside(int[][] image){
		List<List<Integer[]>> cliques = new ArrayList<List<Integer[]>>();
		for(int r=2;r<image.length-1;r++){
			for(int c=2;c<image[0].length-1;c++){
				List<Integer[]> toAdd = new ArrayList<Integer[]>();
					toAdd.clear();
					toAdd.add(new Integer[]{r-1,c});
					toAdd.add(new Integer[]{r,c});
					cliques.add(toAdd);
					toAdd.clear();
					toAdd.add(new Integer[]{r,c});
					toAdd.add(new Integer[]{r+1,c});
					cliques.add(toAdd);
					toAdd.clear();
					toAdd.add(new Integer[]{r,c-1});
					toAdd.add(new Integer[]{r,c});
					cliques.add(toAdd);
					toAdd.clear();
					toAdd.add(new Integer[]{r,c});
					toAdd.add(new Integer[]{r,c+1});
					cliques.add(toAdd);
			}
		}
		for(int r=0;r<=image.length;r++){
			for(int c=0;c<=image[0].length;c++){
				if(r==0 || r==image.length || c==0 || c== image[0].length){
					cliques.addAll(cliqueList(image,c,r));
				}
			}
		}
		return removeDuplicateCliques(cliques);
	}
	private static List<List<Integer[]>> removeDuplicateCliques(List<List<Integer[]>> cliqueList){
		List<List<Integer[]>> cliques = new ArrayList<List<Integer[]>>();
		for(List<Integer[]> clique : cliqueList){
			if(!containsClique(cliques,clique)){
				cliques.add(clique);
			}
		}
		return cliques;
	}
	private static List<Integer[]> getNeighbours8(int x, int y,int[][] image){   
		//List neighbourList = new List();
		List<Integer[]> neighbourList = new ArrayList<Integer[]>();
			for(int row=-1;row<2;row++){
				for(int col=-1;col<2;col++){
					if(row==0&&col==0){continue;}
				Integer[] co = {x+row,y+col};
				if(x+row>0&&y+col>0&&x+row<=image.length && y+col<=image.length){
					neighbourList.add(co);
				}}
			}
		return neighbourList;
	}
	private static List<Integer[]> getNeighbours4(int x, int y,int[][] image){   
		//List neighbourList = new List();
		List<Integer[]> neighbourList = new ArrayList<Integer[]>();
			for(int row=-1;row<2;row++){
				for(int col=-1;col<2;col++){
					if(row==0&&col==0){continue;}
				Integer[] co = {x+row,y+col};
				if(x+row>0&&y+col>0&&x+row<=image.length && y+col<=image.length && row!=col && row!=-col){
					neighbourList.add(co);
				}}
			}
		return neighbourList;
	}
	private static List<Integer[]> getNeighbours(int x, int y,int[][] image,float radius){   
		List<Integer[]> neighbourList = new ArrayList<Integer[]>();
			for(int row=-(int)radius;row<=(int)radius;row++){
				for(int col=-(int)radius;col<=(int)radius;col++){
					if(row==0&&col==0){continue;}
					Integer[] co = {x+row,y+col};
					if(x+row>0&&y+col>0&&x+row<=image.length && y+col<=image.length && (Math.pow(row,2)+Math.pow(col,2))<=Math.pow(radius,2)){
						neighbourList.add(co);
					}	
				}
			}
		return neighbourList;
	}
	private static void readCliqueList(List<List<Integer[]>> cliques){
		for(int i=0;i<cliques.size();i++){
			System.out.println("clique:"+(i+1));
			for(int j=0;j<cliques.get(i).size();j++){
				System.out.println(cliques.get(i).get(j)[0]+","+cliques.get(i).get(j)[1]);  
			}
		}
	}
}
