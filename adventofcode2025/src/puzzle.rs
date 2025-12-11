use std::env;
use std::fmt::Display;

pub fn run<R, M1, M2>(method_a: M1, method_b: M2, input: &Vec<String>)
where
    // In theory we might want different types for A and B.
    // But I can suck it up. It's better if they're the same.
    R: Display,
    M1: FnOnce(&Vec<String>) -> R,
    M2: FnOnce(&Vec<String>) -> R,
{
    let mut args = env::args();
    let result: R;
    match args.nth(1).as_deref() {
        Some("a") => {
            result = method_a(input);
        }
        Some("b") => {
            result = method_b(input);
        }
        _ => {
            panic!("Please specify 'a' or 'b' as the first argument");
        }
    }
    println!("Result: {}", result);
}

pub mod test {
    use crate::input::get_input_from_literal;
    use std::fmt::Debug;

    pub fn run_sample<R: PartialEq + Debug, T: FnOnce(&Vec<String>) -> R>(
        method: T,
        input: &str,
        result: R,
    ) {
        assert_eq!(method(&get_input_from_literal(input)), result);
    }
}
