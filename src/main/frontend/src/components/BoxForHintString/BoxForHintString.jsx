import './BoxForHintString.css'
import '../../styles/boxForHint.css'

function BoxForHintString({ value, category, isRevealed = false }) {
    if (!value) return null;

    return (
        <div className="boxForHint">
            {isRevealed ? (
                <p className="value">{value}</p>
            ) : (
                <div className="questionMarkContainer">
                    <div className="questionMarkHint">?</div>
                    <div className="categoryLabel">{category}</div>
                </div>
            )}
        </div>
    );
}

export default BoxForHintString;