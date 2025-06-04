import './BoxForHintIMG.css'
import '../../styles/boxForHint.css'

function BoxForHintIMG({ src, alt, category, isRevealed = false }) {
    if (!src) return null;

    return (
        <div className="boxForHint">
            {isRevealed ? (
                <img src={src} alt={alt || ''} className="image" />
            ) : (
                <div className="questionMarkContainer">
                    <div className="questionMarkHint">?</div>
                    <div className="categoryLabel">{category}</div>
                </div>
            )}
        </div>
    );
}

export default BoxForHintIMG;