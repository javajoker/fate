<?php

class Date {
	public static function get($year, $month, $day, $hours = 0, $minutes = 0, $seconds = 0) {
		return new Date(mktime ($hours, $minutes, $seconds, $month, $day, $year));
	}
	
	protected $m_time;

	function __construct($time = null) {
		if($time === null) $time = mktime();
		$this->m_time = $time;
	}
	
	public function getTime() {
		return $this->m_time;
	}
	
	public function getYear() {
		return intval(date('Y', $this->m_time));
	}

	public function getMonth() {
		return intval(date('n', $this->m_time));
	}

	public function getDate() {
		return intval(date('j', $this->m_time));
	}
	
	public function getHours() {
		return intval(date('G', $this->m_time));
	}

	public function getMinutes() {
		return intval(date('i', $this->m_time));
	}

	public function getSeconds() {
		return intval(date('s', $this->m_time));
	}
	
	public function toString() {
		return date('Y/n/j G:i:s', $this->m_time);
	}
}
