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
			this.c.add(new SingleCluster(k));
		}

		initCluster();
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
		}
	}

	public void step2() {
		double [][] pi = new double[s.c][k];
		double [] pz = new double[k];

		for (int x=0; x<k; x++) {
			for (int y=0; y<s.c; y++) {
				pi[y][x] = (1.0/k);
			}
			pz[x] = (1.0/k);
		}

		for (int j=0; j<ITER; j++) {
			for (int z=0; z<k; z++) {
				double den = 0.0, num = 0.0;

				for (int x=0; x<s.c; x++) {
					for (int y=0; y<s.a; y++) {
						num += s.xy(x, y)*pz[z]*(this.c.get(z).rx.get(x, 0))*(this.c.get(z).ry.get(y, 0));
						den += s.xy(x, y);
					}
				}

				pz[z] = num/den;
			}

			for (int x=0; x<s.c; x++) {
				double den = 0.0;

				for (int y=0; y<k; y++) {
					den += this.c.get(y).rx.get(x, 0)*pz[y];
				}

				for (int y=0; y<k; y++) {
					pi[x][y] = (this.c.get(y).rx.get(x, 0)*pz[y])/den;
				}
			}
		}

		new Matrix(pi).print(7, 5);

		for (int x=0; x<k; x++) {
			int cs = this.c.get(x).l.size();

			for (int y=0; y<cs; y++) {
				for (int z=0; z<k; z++) {
					this.c.get(x).s[z] += pi[this.c.get(x).l.get(y)][z];
				}
			}

			for (int y=0; y<k; y++) {
				this.c.get(x).s[y] /= cs;
				System.out.println(this.c.get(x).s[y]);
			}
			System.out.println("");
		}

		System.gc();
	}

	public void step3() {
	}

	public void initCluster() {
		Iterator<String> it = this.s.v.keySet().iterator();
		int cn = 0;
		while (it.hasNext()) {
			Vertex v = this.s.v.get(it.next());
			if (v.type == Vertex.CONFERENCE) {
				int clusterNum = cn++%k;
				assignCluster(v, clusterNum);
			}
		}
	}

	public void assignCluster(Vertex v, int n) {
		v.cluster = n;
		this.c.get(n).l.add(v.id);
	}

	public void clearAll() {
		for (int i=0; i<k; i++) {
			this.c.get(i).l.clear();
		}
	}
}
