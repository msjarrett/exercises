use std::fmt::Debug;
use std::fmt::Display;
use std::fs;

pub fn get_input_from_file(path: &str) -> Vec<String> {
    // Placeholder implementation
    get_input_from_literal(&fs::read_to_string(path).expect("Failed to read input file"))
}

pub fn get_input_from_literal(s: &str) -> Vec<String> {
    s.lines().map(|s| s.trim().to_string()).collect()
}
