package com.shuqi.wechat.translater.bean;

import java.util.Map;

public class Word
{
	private String wrod;
	private Map translate;

	public Word() {
	}

	public Word(String wrod, Map translate) {
		this.wrod = wrod;
		this.translate = translate;
	}

	public String getWrod() {
		return wrod;
	}

	public void setWrod(String wrod) {
		this.wrod = wrod;
	}

	public Map getTranslate() {
		return translate;
	}

	public void setTranslate(Map translate) {
		this.translate = translate;
	}
}
