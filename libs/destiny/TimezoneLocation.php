<?php

class TimezoneLocation extends Location {
	private $m_hoursOffset = 0;

	public static $_timezone = array(
		'NZDT' => 13, 
		'IDLE' => 12, 'NZST' => 12, 'NZT' => 12, 
		'AESST' => 11, 
		'CST(ACSST)' => 10.5, 'CADT' => 10.5, 'SADT' => 10.5, 'EST(EAST)' => 10, 'GST' => 10, 'LIGT' => 10, 
		'CAST' => 9.5, 'SAT(SAST)' => 9.5, 'WDT(AWSST)' => 9, 'JST' => 9, 'KST' => 9, 'CCDT' => 9,
		'MT' => 8.5, 'WST(AWST)' => 8, 'CCT' => 8, 
		'JT' => 7.5, 
		'IT' => 3.5, 'BT' => 3, 'EETDST' => 3, 
		'CETDST' => 2, 'EET' => 2, 'FWT' => 2, 'IST' => 2, 'MEST' => 2, 'METDST' => 2, 'SST' => 2, 
		'BST' => 1, 'CET' => 1, 'DNT' => 1, 'FST' => 1, 'MET' => 1, 'MEWT' => 1, 'MEZ' => 1, 'NOR' => 1, 'SET' => 1, 'SWT' => 1, 'WETDST' => 1, 
		'GMT' => 0, 'WET' => 0, 
		'WAT' => -1, 
		'NDT' => -2.5, 
		'ADT' => -3, 'NFT' => -3.5, 'NST' => -3.5, 
		'AST' => -4, 'EDT' => -4, 
		'CDT' => -5, 'EST' => -5, 
		'CST' => -6, 'MDT' => -6, 
		'MST' => -7, 'PDT' => -7, 
		'PST' => -8, 'YDT' => -8, 
		'HDT' => -9, 'YST' => -9, 
		'AHST' => -10, 'CAT' => -10, 
		'NT' => -11, 
		'IDLW' => -12, 
	);

	function __construct($timezone = 'CCT') {
		if(!isset(self::$_timezone[$timezone])) $timezone = 'CCT';
		$this->m_hoursOffset = self::$_timezone[$timezone];
		$this->m_longitude = $this->m_hoursOffset * 15;
	}

	public function getLatitude() {
		return $this->m_latitude;
	}

	public function getLongitude() {
		return $this->m_longitude;
	}

	public function getGMTAbsoluteHoursOffset() {
		return $this->m_longitude / 15;
	}

	public function getGMTHoursOffset() {
		return $this->m_hoursOffset;
	}
}
