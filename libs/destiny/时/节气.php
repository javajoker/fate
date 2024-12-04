<?php
namespace DchLib;

class 节气 {
	// 节气的时间。
	private $m_solarTermDate;
	// 节气名。
	private $m_name;

	public function getSolarTermDate() {
		return $this->m_solarTermDate;
	}

	public function setSolarTermDate($solarTermDate) {
		$this->m_solarTermDate = $solarTermDate;
	}

	public function getName() {
		return $this->m_name;
	}

	public function setName($name) {
		$this->m_name = $name;
	}

}
