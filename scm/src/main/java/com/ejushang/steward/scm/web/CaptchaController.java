package com.ejushang.steward.scm.web;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 图片验证码生成器
 * User: liubin
 * Date: 14-2-7
 */

@Controller
public class CaptchaController {

    private static int WIDTH = 100;
    private static int HEIGHT = 40;
    private static int NUM = 4;

    private static int RED = 240;
    private static int GREEN = 238;
    private static int BLUE = 229;


    /**
     * 生成图片验证码
     *
     * @param response
     * @param request
     * @throws java.io.IOException
     */
    @RequestMapping(value = "/captcha")
    public void randomImageCode(HttpServletResponse response, HttpServletRequest request) throws IOException {
        BufferedImage image = randomImage(response, request);
        ServletOutputStream out = response.getOutputStream();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(image, "JPG", bos);
        byte[] buf = bos.toByteArray();

        response.setContentLength(buf.length);
        out.write(buf);
        bos.close();
        out.close();
    }


    private BufferedImage randomImage(HttpServletResponse response, HttpServletRequest request) {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT,
                BufferedImage.TYPE_INT_RGB);

        Graphics g = image.getGraphics();
        g.setColor(new Color(RED, GREEN, BLUE));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        String code = RandomStringUtils.randomNumeric(NUM);
        for (int i = 0; i < NUM; i++) {
            g.setColor(new Color(0, 0, 0));
            g.setFont(new Font(Integer.valueOf(Font.ITALIC).toString(), Font.ITALIC, HEIGHT + 10));
            g.drawString(code.substring(i, i + 1), (((i * WIDTH) / NUM) * 90) / 100, HEIGHT);
        }
        request.getSession().setAttribute("captcha", code);
        return image;
    }
}