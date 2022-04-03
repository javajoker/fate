<?php

abstract class 数 {
	protected $术数;

	public function __construct($术数) {
		$this->术数 = $术数;
	}

	public function 术数() {
		return $this->术数;
	}

	abstract function setWeight($weight);

	public abstract function getWeight();

	public abstract function __DEBUG();
}
