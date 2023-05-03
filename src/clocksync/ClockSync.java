/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clocksync;

/**
 *
 */
import java.net.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
public class ClockSync {
 private static final int SERVER_PORT = 12345; // Port number for server
 private static final int CLIENT_PORT = 12346; // Port number for client
 
 public static void main(String[] args) throws IOException {
 if (args.length > 0 && args[0].equals("server")) {
 // Start server
 ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
 while (true) {
 Socket clientSocket = serverSocket.accept();
 new Thread(new ServerThread(clientSocket)).start();
 }
 } else {
 // Start client
 DatagramSocket clientSocket = new DatagramSocket(CLIENT_PORT);
 InetAddress serverAddress = InetAddress.getByName(args[0]);
 while (true) {
 long currentTime = System.currentTimeMillis();
 byte[] buffer = ByteBuffer.allocate(Long.SIZE/
8).putLong(currentTime).array();
 DatagramPacket sendPacket = new DatagramPacket(buffer, 
buffer.length, serverAddress, SERVER_PORT);
 clientSocket.send(sendPacket);
     try {
         Thread.sleep(1000);
     } catch (InterruptedException ex) {
         Logger.getLogger(ClockSync.class.getName()).log(Level.SEVERE, null, ex);
     }
 }
 }
 }
 
 private static class ServerThread implements Runnable {
 private Socket clientSocket;
 
 public ServerThread(Socket clientSocket) {
 this.clientSocket = clientSocket;
 }
 
 public void run() {
 try {
 DataInputStream in = new 
DataInputStream(clientSocket.getInputStream());
 while (true) {
 long clientTime = in.readLong();
 long serverTime = System.currentTimeMillis();
 long offset = clientTime - serverTime;
 System.out.println("Offset: " + offset);
 }
 } catch (IOException e) {
 System.err.println(e);
 }
 }
 }
}
