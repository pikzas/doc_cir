package com.bjca.ecopyright.util;

/**
 * 
 *Administrator
 *下午01:36:48
 *
 */
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bjca.ecopyright.statuscode.Constants;



public class GenerateVerifyCode extends HttpServlet
{

     /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String CONTENT_TYPE = "image/png";
     private int                 imgWidth;
     private int                 imgHeight;
     private int                 imgCount;

     private static final Random random       = new Random();

     // private static final MD5 md5 = new MD5();

     public int getImgWidth()
     {
          return imgWidth;
     }

     public void setImgWidth(int imgWidth)
     {
          this.imgWidth = imgWidth;
     }

     public int getImgHeight()
     {
          return imgHeight;
     }

     public void setImgHeight(int imgHeight)
     {
          this.imgHeight = imgHeight;
     }

     public int getImgCount()
     {
          return imgCount;
     }

     public void setImgCount(int imgCount)
     {
          this.imgCount = imgCount;
     }

     // Initialize global variables
     public void init() throws ServletException
     {
          String width = getInitParameter("imageWidth");
          String height = getInitParameter("imageHeight");
          String count = getInitParameter("imageCount");

          imgWidth = 60;
          imgHeight = 20;
          imgCount = 4;

          if (width != null)
          {
               try
               {
                    imgWidth = Integer.parseInt(width);
               }
               catch (Exception ex)
               {
               }
          }
          if (height != null)
          {
               try
               {
                    imgHeight = Integer.parseInt(height);
               }
               catch (Exception ex)
               {
               }
          }
          if (count != null)
          {
               try
               {
                    imgCount = Integer.parseInt(count);
               }
               catch (Exception ex)
               {
               }
          }
     }

     public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
     {
          response.setContentType(CONTENT_TYPE);

          response.setHeader("Pragma", "No-cache");
          response.setHeader("Cache-Control", "no-cache");
          response.setDateHeader("Expires", 0);

          // log.debug("request Referer is : " + request.getHeader("Referer"));

          int width = imgWidth;
          int height = imgHeight;
          BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

          // 获取图形上下文
          Graphics g = image.getGraphics();

          // 设定背景色
          g.setColor(getRandColor(200, 250));
          g.fillRect(0, 0, width, height);

          // 设定字体
          g.setFont(new Font("Times New Roman", Font.PLAIN, 18));

          // 画边框
          // g.setColor(getRandColor(200,250));
          // g.drawRect(0,0,width-1,height-1);

          // 随机产生155条干扰线，使图象中的认证码不易被其它程序探测到
          g.setColor(getRandColor(160, 200));
          for (int i = 0; i < 155; i++)
          {
               int x = random.nextInt(width);
               int y = random.nextInt(height);
               int xl = random.nextInt(12);
               int yl = random.nextInt(12);
               g.drawLine(x, y, x + xl, y + yl);
          }

          // 取随机产生的认证码(4位数字)
          StringBuffer randSB = new StringBuffer();
          for (int i = 0; i < imgCount; i++)
          {
               String rand = String.valueOf(random.nextInt(10));
               randSB.append(rand);
               // 将认证码显示到图象中
               g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110))); // 调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
               g.drawString(rand, 13 * i + 6, 16);
          }

          // 将认证码存入SESSION
          /*
           * String randMD5 = getMD5ofStr(randSB.toString()); log.debug(attrName + "==" + randSB.toString() + "==" + randMD5);
           */
          HttpSession session = request.getSession();
          session.setAttribute(Constants.SESSION_VERIFY_CODE, randSB.toString());

          // 图象生效
          g.dispose();

          // 输出图象到流中
          OutputStream out = response.getOutputStream();
          ImageIO.write(image, "png", out);
          out.close();
     }

     public void destroy()
     {
     }

     /**
      * 给定范围获得随机颜色
      * 
      * @param fc
      * @param bc
      * @return
      */
     private static Color getRandColor(int fc, int bc)
     {
          // Random random = new Random();
          if (fc > 255)
          {
               fc = 255;
          }
          if (bc > 255)
          {
               bc = 255;
          }
          int r = fc + random.nextInt(bc - fc);
          int g = fc + random.nextInt(bc - fc);
          int b = fc + random.nextInt(bc - fc);
          return new Color(r, g, b);
     }

     private static final Log log = LogFactory.getLog(GenerateVerifyCode.class);
}
