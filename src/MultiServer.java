import Firm.Pharmacy;
import Firm.PharmacyChainOperator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MultiServer
{
    private ServerSocket serverSocket;
    private static PharmacyChainOperator pco = new PharmacyChainOperator();
    private static String goJSON;
    private final static String filePath = "info.txt";

    public void start(int port) throws IOException
    {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        try {                                                       // Отсюда
            goJSON = readFile(filePath, StandardCharsets.UTF_8);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        pco = gson.fromJson(goJSON, PharmacyChainOperator.class);    // Досюда

//        pco.addPharmacy("Весна", new Pharmacy("Дарим здоровье", "Ставропольская 101",
//                1, "08:00", "20:00", 20, 1, "Большое белое здание"));
//        pco.addPharmacy("Весна", new Pharmacy("Дарим красоту", "Ялтинская 10", 2,
//                "10:00", "21:00", 10, 0, "Здание на углу, первый этаж"));
//
//        pco.addPharmacy("Здоровье", new Pharmacy("На Севернорной", "Северная 151",
//                1, "06:00", "22:00", 50, 1,
//                "Здание рядом с остановкой троллейбуса 3"));
//        pco.addPharmacy("Здоровье", new Pharmacy("В Галерее", "Северная 313",
//                2, "06:00", "22:00", 550, 0,
//                "Аптека на 3 этаже первой очереди"));
//
//        goJSON = gson.toJson(pco);
//        writeFile(filePath, goJSON);
//        System.out.println("Done!");

        serverSocket = new ServerSocket(port);
        while (true)
        {
            new EchoClientHandler(serverSocket.accept()).start();
        }
    }

    public void stop() throws IOException
    {
        serverSocket.close();
    }

    private static class EchoClientHandler extends Thread
    {
        private final Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        public EchoClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run()
        {
            try
            {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            try
            {
                in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            String inputLine = null;
            while (true)
            {
                try
                {
                    if ((inputLine = in.readLine()) == null) break;
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                if (".".equals(inputLine))
                {
                    out.println("bye");
                    break;
                }
                if ("REFRESH".equals(inputLine))
                {
                    out.println(goJSON);
                }
                if (inputLine != null)
                {
                    if ('d' == inputLine.charAt(0))     // d0,1
                    {
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        String[] ids = inputLine.substring(1).split(",");
                        int groupID = Integer.parseInt(ids[0]);
                        int examID = Integer.parseInt(ids[1]);
                        pco.delPharmacy(groupID, examID);
                        goJSON = gson.toJson(pco);
                        writeFile(filePath, goJSON);
                        out.println(goJSON);
                    }
                    if ('e' == inputLine.charAt(0))     // e0,3##json
                    {
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        String[] parts = inputLine.substring(1).split("##");
                        String[] ids = parts[0].split(",");
                        int groupID = Integer.parseInt(ids[0]);
                        int examID = Integer.parseInt(ids[1]);
                        Pharmacy tempPharmacy = gson.fromJson(parts[1], Pharmacy.class);
                        pco.editPharmacy(groupID, examID, tempPharmacy);
                        goJSON = gson.toJson(pco);
                        writeFile(filePath, goJSON);
                        out.println(goJSON);
                    }
                    if ('u' == inputLine.charAt(0))     // ujson
                    {
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        PharmacyChainOperator tempGo = gson.fromJson(inputLine.substring(1), PharmacyChainOperator.class);
                        pco.setPharmacyChains(tempGo.getPharmacyChains());
                        goJSON = gson.toJson(pco);
                        writeFile(filePath, goJSON);
                    }
                    if ('a' == inputLine.charAt(0))
                    {
                        GsonBuilder gsonBuilder = new GsonBuilder();        // agroupName##json
                        Gson gson = gsonBuilder.create();
                        String[] parts = inputLine.substring(1).split("##");
                        Pharmacy tempPharmacy = gson.fromJson(parts[1], Pharmacy.class);
                        pco.addPharmacy(parts[0], tempPharmacy);
                        goJSON = gson.toJson(pco);
                        writeFile(filePath, goJSON);
                        out.println(goJSON);
                    }
                }
            }

            try
            {
                in.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            out.close();
            try
            {
                clientSocket.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static String readFile(String path, Charset encoding) throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    public static void writeFile(String path, String text)
    {
        try(FileWriter writer = new FileWriter(path, false))
        {
            writer.write(text);
            writer.flush();
        }
        catch(IOException ex)
        {
            System.out.println(ex.getMessage());
        }
    }
}
