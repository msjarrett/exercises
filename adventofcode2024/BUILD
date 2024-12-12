# There is a kt_jvm_binary rule, but it generates a bash script, which isn't so helpful on Windows.
java_binary(
    name = "run_sample",
    args = ["sample"],
    main_class = "com.knottysoftware.exercises.adventofcode2024.AppKt",
    runtime_deps = ["//src/main/kotlin/com/knottysoftware/exercises/adventofcode2024"],
)

java_binary(
    name = "run_input",
    args = ["input"],
    main_class = "com.knottysoftware.exercises.adventofcode2024.AppKt",
    runtime_deps = ["//src/main/kotlin/com/knottysoftware/exercises/adventofcode2024"],
)

java_binary(
    name = "downloader",
    main_class = "com.knottysoftware.exercises.adventofcode.runner.InputDownloaderKt",
    runtime_deps = ["//src/main/kotlin/com/knottysoftware/exercises/adventofcode/runner"],
)

java_test(
    name = "SamplesTest",
    test_class = "com.knottysoftware.exercises.adventofcode2024.SamplesTest",
    runtime_deps = [
        "//src/test/kotlin/com/knottysoftware/exercises/adventofcode2024",
    ],
)

java_test(
    name = "InputsTest",

    # Test needs filesystem access to write $HOME/aoc_cache.
    # Alternatively, we could comment this out, and rely on --spawn_strategy=local on individual
    # executions.
    local = True,
    test_class = "com.knottysoftware.exercises.adventofcode2024.InputsTest",
    runtime_deps = ["//src/test/kotlin/com/knottysoftware/exercises/adventofcode2024"],
)

alias(
    name = "format",
    actual = "//tools/format",
)
