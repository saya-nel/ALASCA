package fr.sorbonne_u.devs_simulation.simulators;

import java.util.concurrent.TimeUnit;

// Copyright Jacques Malenfant, Sorbonne Universite.
//
// Jacques.Malenfant@lip6.fr
//
// This software is a computer program whose purpose is to provide a
// new implementation of the DEVS simulation standard for Java.
//
// This software is governed by the CeCILL-C license under French law and
// abiding by the rules of distribution of free software.  You can use,
// modify and/ or redistribute the software under the terms of the
// CeCILL-C license as circulated by CEA, CNRS and INRIA at the following
// URL "http://www.cecill.info".
//
// As a counterpart to the access to the source code and  rights to copy,
// modify and redistribute granted by the license, users are provided only
// with a limited warranty  and the software's author,  the holder of the
// economic rights,  and the successive licensors  have only  limited
// liability. 
//
// In this respect, the user's attention is drawn to the risks associated
// with loading,  using,  modifying and/or developing or reproducing the
// software by the user in light of its specific status of free software,
// that may mean  that it is complicated to manipulate,  and  that  also
// therefore means  that it is reserved for developers  and  experienced
// professionals having in-depth computer knowledge. Users are therefore
// encouraged to load and test the software's suitability as regards their
// requirements in conditions enabling the security of their systems and/or 
// data to be ensured and,  more generally, to use and operate it in the 
// same conditions as regards security. 
//
// The fact that you are presently reading this means that you have had
// knowledge of the CeCILL-C license and that you accept its terms.

//------------------------------------------------------------------------------
public class Test {

	public Test() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		System.out.println(TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES));
		long conversionFactor = TimeUnit.NANOSECONDS.convert(
				1,
				TimeUnit.MINUTES);
		System.out.println(conversionFactor);
		double delay = 100000.0/Math.PI;
		// mise en nanosecondes
		double d1 = delay*conversionFactor;
		// facteur d'acceleration => delay en nanosecondes
		double temp1 = d1/75.0;
		// repassage en millisecondes
		long d2 = TimeUnit.MILLISECONDS.convert((long)temp1, TimeUnit.NANOSECONDS);
		System.out.println(delay + " --- " + d1 + " --- " + d2);
		System.out.println(delay/75.0);
		System.out.println(delay*60000);

		System.out.println("delai en minutes = " + delay);
		double acc = 75.0;
		double delayAcc = delay/acc;
		System.out.println("delai accelere en minutes = " + delayAcc);
		System.out.println("delai accelere en millisecondes = " + (delayAcc*60000.0));
		double cf1 = TimeUnit.NANOSECONDS.convert(1, TimeUnit.MINUTES);
		double cf2 = TimeUnit.NANOSECONDS.convert(1, TimeUnit.MILLISECONDS);
		System.out.println(delay + " --- " + delayAcc + " --- " +
						   cf1 + " --- " + cf2 + " --- " +
						   ((long)Math.round(((delayAcc*cf1)/cf2))));
	}
}
//------------------------------------------------------------------------------
