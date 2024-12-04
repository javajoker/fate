<?php
namespace DchLib;

class Location {
	protected $m_latitude = 0;
	protected $m_longitude = 0;

	function __construct($latitude, $longitude) {
		$this->m_latitude = $latitude;
		$this->m_longitude = $longitude;
	}

	public function getLatitude() {
		return $this->m_latitude;
	}

	public function getLongitude() {
		return $this->m_longitude;
	}
}
