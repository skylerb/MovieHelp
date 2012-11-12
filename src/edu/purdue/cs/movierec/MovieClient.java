package edu.purdue.cs.movierec;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class MovieClient {
	
	private String server;
	private String title;
	
	Socket socket;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	
	private Handler readHandler;
	private Handler writeHandler;
	
	WriteThread writeThread;
	ReadThread readThread;
	
	public MovieClient(String serverPref, String movieTitle, Handler readHandler) throws UnknownHostException, IOException {
		
		this.server = serverPref;
		this.title = movieTitle;
		
		InetAddress serverAddr = InetAddress.getByName(server);
		this.socket = new Socket(serverAddr, ActivityHome.SERVERPORT);
		
		this.readHandler = readHandler;
		
		oos = new ObjectOutputStream(socket.getOutputStream());
		oos.flush();
		ois = new ObjectInputStream(socket.getInputStream());
		
		writeThread = new WriteThread();
		writeThread.start();
		
		readThread = new ReadThread();
		readThread.start();
		
		synchronized(writeThread) {
			while(writeHandler == null) {
				try {
					writeThread.wait();
				} catch (InterruptedException e) {}
			}
		}
	}
	
	void setReadHandler(Handler h) {
		this.readHandler = h;
	}
	
	void search() {
		writeHandler.post(new Runnable() {
			public void run() {
				try {
					synchronized(socket) {
						oos.writeInt(10);
						oos.writeObject(title);
						oos.flush();
					}
				} catch(IOException ie) {
					//TODO
				}
			}
		});
	}
	
	void movieSaved() {
		writeHandler.post(new Runnable() {
			public void run() {
				try {
					synchronized(socket) {
						oos.writeInt(14);
						oos.flush();
					}
				} catch(IOException ie) {
					//TODO
				}
			}
		});
	}
	
	void answerQuestion(final int response) {
		writeHandler.post(new Runnable() {
			public void run() {
				try {
					synchronized(socket) {
						oos.writeInt(13);
						oos.writeInt(response);
						oos.flush();
					}
				} catch(IOException ie) {
					//TODO
				}
			}
		});
	}
	
	void disconnect() {
		writeHandler.post(new Runnable() {
			public void run() {
				try {
					synchronized(socket) {
						readThread.interrupt();
						oos.close();
						ois.close();
						writeThread.close();
					}
				} catch(Exception ie) {}
			}
		});
	}
	
	void ready() {
		writeHandler.post(new Runnable() {
			public void run() {
				try {
					synchronized(socket) {
						oos.writeInt(12);
						oos.flush();
					}
				} catch(IOException ie) {
					//TODO
				}
			}
		});
	}
	
	private class ReadThread extends Thread {
		public void run() {
			while(!isInterrupted()) {
				try {
					int command = ois.readInt();
					System.out.println("Command: " + command);
					Message msg = readHandler.obtainMessage();
					
					switch(command) {
						case 20: //S_STATUS_OK
							String response = (String)ois.readObject();
							msg.what = command;
							msg.obj = response;
							readHandler.sendMessage(msg);
							break;
						case 21: //S_QUESTION_SEND
							String question = (String)ois.readObject();
							msg.what = command;
							msg.obj = question;
							readHandler.sendMessage(msg);
							break;
						case 22: //S_MOVIE_SEND					
							String title = (String)ois.readObject();
							int year = ois.readInt();
							int mpaaIndex = ois.readInt();
							int mpaa = 0;
							switch(mpaaIndex) {
								case 0: mpaa = R.drawable.g; break;
								case 1: mpaa = R.drawable.pg; break;
								case 2: mpaa = R.drawable.pg13; break;
								case 3: mpaa = R.drawable.r; break;
								case 4: mpaa = R.drawable.nc17; break;
								case 5: mpaa = R.drawable.approved; break;
								case 6: mpaa = R.drawable.tv14; break;
								case 7: mpaa = R.drawable.ur; break;
								case 8: mpaa = R.drawable.nr; break;
								default: mpaa = R.drawable.ur; break;
							}
							List<String> genres = (List<String>)ois.readObject();							
							int filelength = ois.readInt();
							byte[] buf = (byte[])ois.readObject();
							Bitmap bmp = BitmapFactory.decodeByteArray(buf, 0, filelength);
							Movie m = new Movie(title, mpaa, year, genres, bmp);
							msg.what = command;
							msg.obj = m;
							readHandler.sendMessage(msg);
							break;
						case 23: //S_ALL_MOVIES_SENT
							msg.what = command;
							readHandler.sendMessage(msg);
							break;
						case 30: //S_ERROR_MOVIEDOESNOTEXIST
							msg.what = command;
							readHandler.sendMessage(msg);
							disconnect();
							break;
					}
				} catch(Exception e) {
					//TODO
				}
			}
		}
	}
	
	private class WriteThread extends Thread {
		public void run() {
			Looper.prepare();
			synchronized(this) {
				writeHandler = new Handler();
				notifyAll();
			}
			Looper.loop();
		}
		
		public void close() {
			Looper.myLooper().quit();
		}
	}
 
}

