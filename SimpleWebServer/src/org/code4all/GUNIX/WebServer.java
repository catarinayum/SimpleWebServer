package org.code4all.GUNIX;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {
    public static void main(String[] args) throws IOException {

        // instantiates server socket and assigns port
        ServerSocket serverSocket = new ServerSocket(8080);


        while (true) {
            System.out.println("Server is awaiting connection...");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Server has accepted a connection.");
            handler(clientSocket);
            System.out.println("Request has been handled.");
            clientSocket.close();
            System.out.println("Client socket is closed.");
            break;
        }
    }

    public static void handler(Socket clientSocket) {

        File htmlFile = new File("SimpleWebServer/resources/index.html");

        BufferedReader br = null;

        OutputStream out = null;

        try {
            // receives input stream from socket and reads what is received
            br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // output stream to client
            out = clientSocket.getOutputStream();

            // reads and turns input into string
            String in = br.readLine();
            String responseHeader = "";

            if (in.startsWith("GET / HTTP/1.1")) {
                if (!htmlFile.exists()) {
                    responseHeader = "HTTP/1.1 404 Not Found\r\n" +
                            "Content-Type: text/html; charset=UTF-8\r\n" +
                            "Content-Length: 0\r\n" +
                            "\r\n";

                    out.write(responseHeader.getBytes());
                    out.close();
                    return;
                }
                responseHeader = "HTTP/1.0 200 Document Follows\r\n" +
                        "Content-Type: text/html; charset=UTF-8\r\n" +
                        // file.length() will return byte size of file
                        "Content-Length: " + htmlFile.length() + "\r\n" +
                        "\r\n";

                out.write(responseHeader.getBytes());

                FileInputStream fileInputStream = new FileInputStream(htmlFile);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                fileInputStream.close();
            }

        } catch (
                IOException e) {
            System.out.println("Error occurred: " + e.getMessage());
            return;

        } finally {
            try {
                br.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

