<?php

class 术数 {
	private $m_index;
	private $m_element;
	private $m_yinYang;

	public function __construct($element, $yinYang, $index = -1) {
		$this->m_element = $element;
		$this->m_yinYang = $yinYang;
		$this->m_index = $index;
		
		if($index != -1)
			$this->m_yinYang = (abs($index % 2) == 1) ? 阴阳::$阳 : 阴阳::$阴;
	}

	public function getIndex() {
		return $this->m_index;
	}

	public function 五行() {
		return $this->m_element;
	}

	public function 阴阳() {
		return $this->m_yinYang;
	}

	public function equals($obj) {
		if (!($obj instanceof 术数))
			return false;

		$ret = ($this->m_element === $obj->m_element && $this->m_yinYang === $obj->yinYang);
		if ($this->m_index >= 0 && $obj->m_index >= 0)
			$ret = $ret && ($this->m_index == $obj->m_index);

		return ret;
	}
}
