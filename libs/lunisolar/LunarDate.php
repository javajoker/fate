<?php

class LunarDate {
	private $m_year;
	private $m_month;
	private $m_date;

	function __construct($year, $month, $date) {
		$this->m_year = $year;
		$this->m_month = $month;
		$this->m_date = $date;
	}

	public function getYear() {
		return $this->m_year;
	}

	public function setYear($year) {
		$this->m_year = $year;
	}

	public function getMonth() {
		return $this->m_month;
	}

	public function setMonth($month) {
		$this->m_month = $month;
	}

	public function getDate() {
		return $this->m_date;
	}

	public function setDate($date) {
		$this->m_date = $date;
	}
}
