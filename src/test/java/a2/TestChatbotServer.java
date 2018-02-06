package a2;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestChatbotServer {
  @Mock
  private Chatbot mockBot;
  @Mock
  private ServerSocket mockServerSocket;
  @Mock
  private Socket mockSocket;

  private ChatbotServer chatServer;

  @Before
  public void setUp () {
    chatServer = new ChatbotServer(mockBot, mockServerSocket);
  }

  @Test
  public void testChatbotServer () throws IOException {
    OutputStream dout = new ByteArrayOutputStream();
    InputStream din = new ByteArrayInputStream("CALL\nCALL\n".getBytes());

    when(mockServerSocket.accept()).thenReturn(mockSocket);
    when(mockSocket.getInputStream()).thenReturn(din);
    when(mockSocket.getOutputStream()).thenReturn(dout);
    try {
      when(mockBot.getResponse("CALL")).thenReturn("RESPONSE");
    } catch (AIException aie) {
      System.err.println(aie);
    }

    chatServer.handleOneClient();

    BufferedReader buffed = new BufferedReader(new StringReader(dout.toString()));
    String line;
    do {
      line = buffed.readLine();
      if (line.equals("null"))
        break;

      assertEquals("RESPONSE", line);
    } while (true);
  }

  @Test
  public void testAIException () throws IOException {
    OutputStream dout = new ByteArrayOutputStream();
    InputStream din = new ByteArrayInputStream("ERROR\n".getBytes());

    when(mockServerSocket.accept()).thenReturn(mockSocket);
    when(mockSocket.getOutputStream()).thenReturn(dout);
    when(mockSocket.getInputStream()).thenReturn(din);

    try {
      doThrow(new AIException("BROKEN AI")).when(mockBot).getResponse("ERROR");
    } catch (AIException aie){
      System.err.println(aie);
    }

    chatServer.handleOneClient();

    BufferedReader buffed = new BufferedReader(new StringReader(dout.toString()));
    assertEquals("Got AIException: <BROKEN AI>", buffed.readLine());
  }

  @Test
  public void testBrokenSocket () {
    try {
      doThrow(new SocketException()).when(mockServerSocket).accept();
    } catch (IOException ioe) {
      System.err.println(ioe);
    }

    chatServer.handleOneClient();
  }

}