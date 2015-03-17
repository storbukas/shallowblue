package no.uib.inf112.group4.tests;

import static org.junit.Assert.assertNotNull;
import no.uib.inf112.group4.framework.network.Client;
import no.uib.inf112.group4.framework.network.NetworkHandshake;
import no.uib.inf112.group4.framework.network.Server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class NetworkTest {

	Server server;
	Client client;
	Thread t1;
	Thread t2;

	@Before
	public void setUp() throws InterruptedException {
		t1 = new Thread(new Runnable() {
			public void run() {
				server = new Server(new NetworkHandshake(0, null), 9876);
			}
		});
		t1.start();
		t2 = new Thread(new Runnable() {
			public void run() {
				client = new Client(new NetworkHandshake(0, null), "localhost",
						9876);
			}
		});
		t2.start();
		t1.join();
		t2.join();
	}

	@After
	public void tearDown() {
		server = null;
		client = null;
		t1 = null;
		t2 = null;
	}

	@Test
	public void testNetworkHandShake() {
		assertNotNull(client.getHandshake());
		assertNotNull(server.getHandshake());
	}
}
