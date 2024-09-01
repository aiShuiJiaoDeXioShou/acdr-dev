import os
import cairosvg

def convert_svg_to_png(svg_folder):
    # 遍历指定文件夹中的所有文件
    for filename in os.listdir(svg_folder):
        # 检查文件是否为 SVG 文件
        if filename.endswith('.svg'):
            svg_path = os.path.join(svg_folder, filename)
            png_filename = filename.replace('.svg', '.png')
            png_path = os.path.join(svg_folder, png_filename)

            # 使用 CairoSVG 将 SVG 文件转换为 PNG 文件
            cairosvg.svg2png(url=svg_path, write_to=png_path)
            print(f"Converted {svg_path} to {png_path}")

if __name__ == "__main__":
    svg_folder = "../src/static/tabbar"  # 将此路径替换为你的 SVG 文件夹路径
    convert_svg_to_png(svg_folder)
