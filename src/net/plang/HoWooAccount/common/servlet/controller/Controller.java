package net.plang.HoWooAccount.common.servlet.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.plang.HoWooAccount.common.servlet.ModelAndView;

public interface Controller {
    ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response);
}
