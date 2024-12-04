<?php
namespace DchLib;

/**
 * 皆以天干论术数，地支阴阳取藏干法，地支六合化阴阳取天干引
 */
class 干支数 {
	private $干支;

	private $干数;
	private $支数;

	public function __construct($干支) {
		$this->干数 = new 干数($干支->天干());
		$this->支数 = new 支数($干支->地支());

		$this->干支 = $干支;
	}

	public function 干支() {
		return $this->干支;
	}

	public function 干数() {
		return $this->干数;
	}

	public function 支数() {
		return $this->支数;
	}
}
