use std::env;
use std::fs;
use std::path::Path;

pub fn get_input_for_day(year: u16, day: u8) -> Vec<String> {
    let path = Path::new(&env::var("HOME").unwrap()).join("aoc_cache");
    // Ensure cache directory exists, without creating all of $HOME.
    match fs::create_dir(&path) {
        Ok(_) => {}
        Err(e) if e.kind() == std::io::ErrorKind::AlreadyExists => {}
        Err(e) => panic!("Failed to create cache directory: {}", e),
    }

    let session = fs::read_to_string(path.join("session")).expect("Unable to read session token");

    let path = path.join(year.to_string());
    fs::create_dir_all(&path).expect("Failed to create year {year}.");

    let path = path.join(day.to_string());
    if !path.is_file() {
        let client = reqwest::blocking::Client::new();
        let request = client
            .get(format!("https://adventofcode.com/{year}/day/{day}/input"))
            .header("Cookie", format!("session={}", session.trim()));
        let mut result = request.send().unwrap(); // It's an Ok even for a failed HTTP status code.
        println!("Download: {:?} ", result);
        assert!(result.status().is_success());

        let mut file = fs::File::create(&path).expect("Unable to create input file");
        std::io::copy(&mut result, &mut file).expect("Unable to write input file");
    }

    get_input_from_file(path.to_str().unwrap())
}

pub fn get_input_from_file(path: &str) -> Vec<String> {
    // Placeholder implementation
    get_input_from_literal(&fs::read_to_string(path).expect("Failed to read input file"))
}

pub fn get_input_from_literal(s: &str) -> Vec<String> {
    s.lines().map(|s| s.trim().to_string()).collect()
}
