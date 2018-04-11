package com.rao.tools;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

import javax.imageio.stream.FileImageInputStream;

public class ConfManagerTool {
	public static ArrayList<String> fileArrayList = new ArrayList();

	/**
	 * @param args
	 */
	public class MyFileVisitor extends SimpleFileVisitor<Path> {
		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
				throws IOException {

			ConfManagerTool.fileArrayList.add(file.toString());
			return FileVisitResult.CONTINUE;
		}
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		String CurDir = System.getProperty("user.dir");
		Path path = Paths.get(CurDir, "conf");
		int userInputIndex;
		String fileContents;

		char[] cbuf = new char[1024];

		Files.walkFileTree(path, new ConfManagerTool().new MyFileVisitor());
		while (true) {
			System.out.println("请根据屏幕提示选择对应的配置文件");

			

			for (int i = 0; i < ConfManagerTool.fileArrayList.size(); i++) {
				// System.out.println(ConfManagerTool.fileArrayList.get(i));
				System.out.println(String.format("按 %d ，使用如下配置%s", i,
						ConfManagerTool.fileArrayList.get(i)));

			}
			// 使用BufferedReader和InputStreamReader读取用户的一次性输入内容
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			userInputIndex = Integer.parseInt(br.readLine());
			// 读取用户选择的配置文件的全部内容
			File f = new File(ConfManagerTool.fileArrayList.get(userInputIndex));
			try (FileChannel inChannel = new FileInputStream(f).getChannel();
					
							) {
				// 将FileChannel里的数据全部映射成ByteBuffer
				MappedByteBuffer buffer = inChannel.map(
						FileChannel.MapMode.READ_ONLY, 0, f.length());
				// 使用GBK的字符集来创建解码器
				Charset csGBK = Charset.forName("GBK");
				CharsetDecoder csGBKd = csGBK.newDecoder();
				CharBuffer cb = csGBKd.decode(buffer);

				fileContents = cb.toString();
				System.out.println(fileContents);
				Runtime r = Runtime.getRuntime();

				r.exec("cmd.exe /c  start binTools\\"+ fileContents);



			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e);
			}

		}
	}

}
