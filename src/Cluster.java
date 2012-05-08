/**
 * Cluster - Cluster store for the program
 */

import Jama.*;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * @author pksunkara
 */
public class Cluster {

	public Store s = null;
	public int k = 0;
	public double a = 1.0;

	public final int ITER = 5;

	public ArrayList<SingleCluster> c = null;

	public Cluster(int k, Store s, int a) {
		this.k = k;
		this.s = s;
		this.a = ((double) a)/100;

		this.c = new ArrayList<SingleCluster>();
		for (int i=0; i<k; i++) {
			this.c.add(new SingleCluster());
		}

		randomize();
	}

	public void iterate() {
		step1();
		step2();
		step3();
	}

	public void step1() {
		for (int i=0; i<k; i++) {
			int cs = this.c.get(i).l.size();
			double [][] xy = new double[cs][s.a];

			for (int x=0; x<cs; x++) {
				for (int y=0; y<s.a; y++) {
					xy[x][y] = s.xy(this.c.get(i).l.get(x), y);
				}
			}

			Matrix mxy = new Matrix(s.wxy);
			Matrix wxy = new Matrix(xy);
			Matrix wyy = new Matrix(s.wyy);

			Matrix rym = wxy.transpose().times(this.a).times(wxy).plus(wyy.times(1-this.a));
			Matrix rxm = wxy.times(this.a).times((Matrix.identity(s.a, s.a).minus(wyy.times(1-this.a))).inverse()).times(wxy.transpose());

			Matrix erx = rxm.eig().getV();
			Matrix ery = rym.eig().getV();

			this.c.get(i).ri = erx.getMatrix(0,erx.getRowDimension()-1,0,0);
			this.c.get(i).ry = ery.getMatrix(0,ery.getRowDimension()-1,0,0);
			this.c.get(i).rx = mxy.times(this.c.get(i).ry);

			System.gc();
			System.exit(0);
		}
	}

	public void step2() {
		double [][] pio = new double[s.a][k];
		double [][] pin = new double[s.a][k];

		double [] pzo = new double[k];
		double [] pzn = new double[k];

		for (int x=0; x<k; x++) {
			for (int y=0; y<s.a; y++) {
				pio[y][x] = (1.0/k);
			}
			pzo[x] = (1.0/k);
		}

		for (int j=0; j<ITER; j++) {
			for (int z=0; z<k; z++) {
				double den = 0.0, num = 0.0;

				for (int x=0; x<s.c; x++) {
					for (int y=0; y<s.c; y++) {
						num += s.xy(x, y)*pzo[z];//*(this.c.get(z).rx[][]);
						den += s.xy(x, y);
					}
				}

				pzn[z] = 0.0;
			}
		}

		System.gc();
	}

	public void step3() {

	}

	public void randomize() {
		Iterator<String> it = this.s.v.keySet().iterator();
		while (it.hasNext()) {
			Vertex v = this.s.v.get(it.next());
			if (v.type == Vertex.CONFERENCE) {
				int clusterNum = (int) (Math.floor(Math.random() * this.k));
				v.cluster = clusterNum;
				this.c.get(clusterNum).l.add(v.id);
			}
		}

		for (int i=0; i<k; i++) {
			if (this.c.get(i).l.isEmpty()) {
				clearAll();
				randomize();
				break;
			}
		}
	}

	public void clearAll() {
		for (int i=0; i<k; i++) {
			this.c.get(i).l.clear();
		}
	}
}
