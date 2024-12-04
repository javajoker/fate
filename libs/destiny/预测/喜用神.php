<?php
namespace DchLib;

class 喜用神 {
	private $用神 = array();
	private $忌神 = null;

	public function __construct($用神) {
		if (count($用神) > 1)
			$this->用神 = $用神;
		else
			$this->用神 = array( $用神[0], $用神[0]->印() );
	}

	public function 用神集() {
		return $this->用神;
	}

	public function 用神() {
		return $this->用神[0];
	}

	public function 喜神() {
		return $this->用神[1];
	}

	public function 喜神2() {
		if (count( $this->用神 ) > 2)
			return $this->用神[2];
		else
			return null;
	}

	public function 忌神($忌神 = null) {
		// get
		if($忌神 === null) {
			return ($this->忌神 == null) ? $this->喜神()->官() : $this->忌神;
		}
		// set
		$this->忌神 = $忌神;
	}
}
