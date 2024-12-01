load("@rules_kotlin//kotlin:jvm.bzl", "kt_jvm_library")

# There is a kt_jvm_binary rule, but it generates a bash script, which isn't so helpful on Windows.
java_binary(
    name = "app",
    main_class = "com.knottysoftware.exercises.adventofcode2024.AppKt",
    runtime_deps = ["//src/main/kotlin/com/knottysoftware/exercises/adventofcode2024"],
)

java_binary(
    name = "downloader",
    main_class = "com.knottysoftware.exercises.adventofcode.InputDownloaderKt",
    runtime_deps = ["//src/main/kotlin/com/knottysoftware/exercises/adventofcode"],
)

java_test(
    name = "SamplesTest",
    test_class = "com.knottysoftware.exercises.adventofcode2024.SamplesTest",
    runtime_deps = ["//src/test/kotlin/com/knottysoftware/exercises/adventofcode2024"],
)

java_test(
    name = "InputsTest",
    test_class = "com.knottysoftware.exercises.adventofcode2024.InputsTest",
    runtime_deps = ["//src/test/kotlin/com/knottysoftware/exercises/adventofcode2024"],
)
