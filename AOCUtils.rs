pub fn file_to_string_vector(path: &str) -> Option<Vec<String>> {
    let result: Result<String, std::io::Error> = std::fs::read_to_string(path);
    match result {
        Ok(content) => {
            let mut strs: Vec<String> = Vec::new();
            let mut prev = 0;
            for (i, c) in content.chars().enumerate() {
                if c == '\n' {
                    strs.push(String::from(&content[prev..i]));
                    prev = i + 1;
                }
            }
            if prev != content.len() {
                strs.push(String::from(&content[prev..content.len()]));
            }
            return Some(strs);
        }
        Err(e) => {
            println!("An error occured while reading the input: {}", e);
            return None;
        }
    }
}